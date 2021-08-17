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

import java.util.Date;
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
		given(this.workLogService.findHoursLoggedByEmployeeAtDate(EMPLOYEE_ID, new Date())).willReturn(0);
	}

	////////////////////////////////////////////////////////////////////////////////
	// Tests: Create

	@WithMockUser(value = "spring")
	@Test
	void testInitCreationForm() throws Exception {
		mockMvc.perform(get("/myTasks/{taskId}/workLog", TASK_ID))
				// result
				.andExpect(status().isOk()).andExpect(model().attributeExists("workLog"))
				.andExpect(view().name("workLogs/createOrUpdateWorkLogForm"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessCreationFormSuccess() throws Exception {
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
	void testProcessCreationFormHasErrors() throws Exception {
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

}
