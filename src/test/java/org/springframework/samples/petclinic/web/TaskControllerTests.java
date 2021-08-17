package org.springframework.samples.petclinic.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Building;
import org.springframework.samples.petclinic.model.Employee;
import org.springframework.samples.petclinic.model.Task;
import org.springframework.samples.petclinic.model.Tool;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.service.BuildingService;
import org.springframework.samples.petclinic.service.EmployeeService;
import org.springframework.samples.petclinic.service.TaskService;
import org.springframework.samples.petclinic.service.ToolService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Optional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(controllers = TaskController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
class TaskControllerTests {

	////////////////////////////////////////////////////////////////////////////////
	// Initialize

	private static final int TASK_ID = 1;
	private static final int EMPLOYEE_ID = 1;
	private static final int BUILDING_ID = 1;
	private static final int TOOL_ID = 1;

	// @Autowired
	// private TaskController taskController;

	@MockBean
	private TaskService taskService;

	@MockBean
	private EmployeeService employeeService;

	@MockBean
	private ToolService toolService;

	@MockBean
	private BuildingService buildingService;

	@Autowired
	private MockMvc mockMvc;

	private Task task;
	private Employee employee;
	private Building building;
	private Tool tool;

	@BeforeEach
	void setup() {
		building = new Building();
		building.setId(BUILDING_ID);
		building.setName("Building");
		building.setAddress("c/Building");
		building.setIncome(0);
		given(this.buildingService.findBuildingById(BUILDING_ID)).willReturn(Optional.of(building));
		tool = new Tool();
		tool.setId(TOOL_ID);
		tool.setName("Tool");
		tool.setBuilding(building);
		building.addTool(tool);
		given(this.toolService.findToolById(TOOL_ID)).willReturn(Optional.of(tool));
		employee = new Employee();
		employee.setId(EMPLOYEE_ID);
		employee.setName("Employee");
		employee.setEmail("employee@employee.com");
		employee.setAddress("c/Employee");
		employee.setBuilding(building);
		building.addEmployee(employee);
		given(this.employeeService.findEmployeeById(EMPLOYEE_ID)).willReturn(Optional.of(employee));
		given(this.employeeService.findEmployeePrincipal()).willReturn(Optional.of(employee));
		task = new Task();
		task.setId(TASK_ID);
		task.setName("Task");
		task.setIncome(0);
		task.setComplete(false);
		given(this.taskService.findTaskById(TASK_ID)).willReturn(Optional.of(task));
	}

	////////////////////////////////////////////////////////////////////////////////
	// Tests: List unassigned

	@WithMockUser(value = "spring")
	@Test
	void testListUnassignedTasks() throws Exception {
		mockMvc.perform(get("/tasks/unassignedTasks"))
				// result
				.andExpect(status().isOk()).andExpect(model().attributeExists("selections"))
				.andExpect(view().name("tasks/tasksList"));
	}

	////////////////////////////////////////////////////////////////////////////////
	// Tests: List uncomplete

	@WithMockUser(value = "spring")
	@Test
	void testListUncompleteTasks() throws Exception {
		mockMvc.perform(get("/tasks/uncompleteTasks"))
				// result
				.andExpect(status().isOk()).andExpect(model().attributeExists("selections"))
				.andExpect(view().name("tasks/tasksList"));
	}

	////////////////////////////////////////////////////////////////////////////////
	// Tests: List mine

	@WithMockUser(value = "spring")
	@Test
	void testListMyTasks() throws Exception {
		mockMvc.perform(get("/myTasks"))
				// result
				.andExpect(status().isOk()).andExpect(model().attributeExists("selections"))
				.andExpect(view().name("myTasks/tasksList"));
	}

	////////////////////////////////////////////////////////////////////////////////
	// Tests: Show

	@WithMockUser(value = "spring")
	@Test
	void testShowTask() throws Exception {
		mockMvc.perform(get("/tasks/{taskId}", TASK_ID))
				// result
				.andExpect(status().isOk()).andExpect(model().attribute("task", hasProperty("name", is("Task"))))
				.andExpect(view().name("tasks/taskDetails"));
	}

	////////////////////////////////////////////////////////////////////////////////
	// Tests: Create

	@WithMockUser(value = "spring")
	@Test
	void testInitCreationForm() throws Exception {
		mockMvc.perform(get("/tasks/new"))
				// result
				.andExpect(status().isOk()).andExpect(model().attributeExists("task"))
				.andExpect(view().name("tasks/createOrUpdateTaskForm"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessCreationFormSuccess() throws Exception {
		mockMvc.perform(post("/tasks/new")
				// params
				.param("name", "Task")
				// other
				.with(csrf()))
				// result
				.andExpect(status().is3xxRedirection());
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessCreationFormHasErrors() throws Exception {
		mockMvc.perform(post("/tasks/new")
				// params
				.param("name", "")
				// other
				.with(csrf()))
				// result
				.andExpect(status().isOk()).andExpect(model().attributeHasErrors("task"))
				.andExpect(model().attributeHasFieldErrors("task", "name"))
				.andExpect(view().name("tasks/createOrUpdateTaskForm"));
	}

	////////////////////////////////////////////////////////////////////////////////
	// Tests: Complete

	@WithMockUser(value = "spring")
	@Test
	void testCompleteTask() throws Exception {
		mockMvc.perform(get("/myTasks/{taskId}/complete", TASK_ID))
				// result
				.andExpect(view().name("myTasks/tasksList"));
	}

	////////////////////////////////////////////////////////////////////////////////
	// Tests: Assign employee

	@WithMockUser(value = "spring")
	@Test
	void testAssignEmployeeToTask() throws Exception {
		mockMvc.perform(get("/tasks/{taskId}/assignEmployee/{employeeId}", TASK_ID, EMPLOYEE_ID))
				// result
				.andExpect(view().name("tasks/tasksList"));
	}

	////////////////////////////////////////////////////////////////////////////////
	// Tests: Assign tool

	@WithMockUser(value = "spring")
	@Test
	void testAssignToolToTask() throws Exception {
		task.addEmployee(employee);
		employee.addTask(task);
		mockMvc.perform(get("/tasks/{taskId}/assignTool/{toolId}", TASK_ID, TOOL_ID))
				// result
				.andExpect(view().name("tasks/tasksList"));
	}

}
