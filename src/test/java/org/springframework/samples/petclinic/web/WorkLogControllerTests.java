package org.springframework.samples.petclinic.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Employee;
import org.springframework.samples.petclinic.model.Task;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.BDDMockito.given;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.service.EmployeeService;
import org.springframework.samples.petclinic.service.TaskService;
import org.springframework.samples.petclinic.service.WorkLogService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Optional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(controllers = WorkLogController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
class WorkLogControllerTests {

	////////////////////////////////////////////////////////////////////////////////
	// Initialize

	private static final int TASK_ID = 1;

	private static final int EMPLOYEE_ID = 1;

	// @Autowired
	// private WorkLogController workLogController;

	@MockBean
	private WorkLogService workLogService;

	@MockBean
	private TaskService taskService;

	@MockBean
	private EmployeeService employeeService;

	@Autowired
	private MockMvc mockMvc;

	private Task task;

	private Employee employee;

	@BeforeEach
	void setup() {
		task = new Task();
		task.setId(TASK_ID);
		task.setComplete(false);
		employee = new Employee();
		employee.setId(EMPLOYEE_ID);
		task.addEmployee(employee);
		employee.addTask(task);
		given(this.taskService.findTaskById(TASK_ID)).willReturn(Optional.of(task));
		given(this.employeeService.findEmployeeById(EMPLOYEE_ID)).willReturn(Optional.of(employee));
		given(this.employeeService.findEmployeePrincipal()).willReturn(Optional.of(employee));
		given(this.workLogService.findHoursLoggedByEmployeeToday(EMPLOYEE_ID)).willReturn(0);
	}

	////////////////////////////////////////////////////////////////////////////////
	// Tests: Create

	@WithMockUser(value = "spring")
	@Test
	void testInitCreationForm() throws Exception {
		mockMvc.perform(get("/myTasks/{taskId}/workLog", TASK_ID))
				// result
				.andExpect(status().isOk()).andExpect(model().attributeDoesNotExist("error")).andExpect(model().attributeExists("workLog"))
				.andExpect(view().name("workLogs/createOrUpdateWorkLogForm"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testInitCreationFormWithTaskWithWrongTaskId() throws Exception {
		mockMvc.perform(get("/myTasks/{taskId}/workLog", 0))
				// result
				.andExpect(status().isOk()).andExpect(model().attributeExists("error"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testInitCreationFormWithTaskNotAssignedToEmployee() throws Exception {
		task.removeEmployee(employee);
		employee.removeTask(task);
		mockMvc.perform(get("/myTasks/{taskId}/workLog", TASK_ID))
				// result
				.andExpect(status().isOk()).andExpect(model().attributeExists("error"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testInitCreationFormWithCompleteTask() throws Exception {
		task.setComplete(true);
		mockMvc.perform(get("/myTasks/{taskId}/workLog", TASK_ID))
				// result
				.andExpect(status().isOk()).andExpect(model().attributeExists("error"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testInitCreationFormWithTooManyHours() throws Exception {
		given(this.workLogService.findHoursLoggedByEmployeeToday(EMPLOYEE_ID)).willReturn(8);
		mockMvc.perform(get("/myTasks/{taskId}/workLog", TASK_ID))
				// result
				.andExpect(status().isOk()).andExpect(model().attributeExists("error"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessCreationFormSuccessWithZeroHours() throws Exception {
		given(this.workLogService.findHoursLoggedByEmployeeToday(EMPLOYEE_ID)).willReturn(8);
		mockMvc.perform(post("/myTasks/{taskId}/workLog", TASK_ID)
				// params
				.param("hours", "0")
				// other
				.with(csrf()))
				// result
				.andExpect(status().is3xxRedirection());
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessCreationFormSuccessWithFourHours() throws Exception {
		given(this.workLogService.findHoursLoggedByEmployeeToday(EMPLOYEE_ID)).willReturn(4);
		mockMvc.perform(post("/myTasks/{taskId}/workLog", TASK_ID)
				// params
				.param("hours", "4")
				// other
				.with(csrf()))
				// result
				.andExpect(status().is3xxRedirection());
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessCreationFormSuccessWithEightHours() throws Exception {
		given(this.workLogService.findHoursLoggedByEmployeeToday(EMPLOYEE_ID)).willReturn(0);
		mockMvc.perform(post("/myTasks/{taskId}/workLog", TASK_ID)
				// params
				.param("hours", "8")
				// other
				.with(csrf()))
				// result
				.andExpect(status().is3xxRedirection());
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessCreationFormNotEnoughHours() throws Exception {
		mockMvc.perform(post("/myTasks/{taskId}/workLog", TASK_ID)
				// params
				.param("hours", "-1")
				// other
				.with(csrf()))
				// result
				.andExpect(status().isOk()).andExpect(model().attributeHasErrors("workLog"))
				.andExpect(model().attributeHasFieldErrors("workLog", "hours"))
				.andExpect(view().name("workLogs/createOrUpdateWorkLogForm"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessCreationFormTooManyHoursAlready() throws Exception {
		given(this.workLogService.findHoursLoggedByEmployeeToday(EMPLOYEE_ID)).willReturn(4);
		// el worklog en sí está bien
		// lo que debe fallar es que sus horas más las ya registradas dan demasiadas
		mockMvc.perform(post("/myTasks/{taskId}/workLog", TASK_ID)
				// params
				.param("hours", "5")
				// other
				.with(csrf()))
				// result
				.andExpect(status().isOk()).andExpect(model().attributeExists("error"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessCreationFormTooManyHours() throws Exception {
		mockMvc.perform(post("/myTasks/{taskId}/workLog", TASK_ID)
				// params
				.param("hours", "9")
				// other
				.with(csrf()))
				// result
				.andExpect(status().isOk()).andExpect(model().attributeHasErrors("workLog"))
				.andExpect(model().attributeHasFieldErrors("workLog", "hours"))
				.andExpect(view().name("welcome"));
	}

}
