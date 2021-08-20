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
	private static final int EMPLOYEE_1_ID = 1;
	private static final int EMPLOYEE_2_ID = 2;
	private static final int BUILDING_1_ID = 1;
	private static final int BUILDING_2_ID = 2;
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
	private Employee employee1;
	private Employee employee2;
	private Building building1;
	private Building building2;
	private Tool tool;

	@BeforeEach
	void setup() {
		building1 = new Building();
		building1.setId(BUILDING_1_ID);
		building1.setName("Building");
		building1.setAddress("c/Building");
		building1.setIncome(0);
		given(this.buildingService.findBuildingById(BUILDING_1_ID)).willReturn(Optional.of(building1));
		building2 = new Building();
		building2.setId(BUILDING_2_ID);
		building2.setName("Building");
		building2.setAddress("c/Building");
		building2.setIncome(0);
		given(this.buildingService.findBuildingById(BUILDING_2_ID)).willReturn(Optional.of(building2));
		tool = new Tool();
		tool.setId(TOOL_ID);
		tool.setName("Tool");
		tool.setBuilding(building1);
		building1.addTool(tool);
		given(this.toolService.findToolById(TOOL_ID)).willReturn(Optional.of(tool));
		employee1 = new Employee();
		employee1.setId(EMPLOYEE_1_ID);
		employee1.setName("Employee");
		employee1.setEmail("employee@employee.com");
		employee1.setAddress("c/Employee");
		employee1.setBuilding(building1);
		building1.addEmployee(employee1);
		given(this.employeeService.findEmployeeById(EMPLOYEE_1_ID)).willReturn(Optional.of(employee1));
		given(this.employeeService.findEmployeePrincipal()).willReturn(Optional.of(employee1));
		employee2 = new Employee();
		employee2.setId(EMPLOYEE_2_ID);
		employee2.setName("Employee");
		employee2.setEmail("employee@employee.com");
		employee2.setAddress("c/Employee");
		employee2.setBuilding(building2);
		building2.addEmployee(employee2);
		given(this.employeeService.findEmployeeById(EMPLOYEE_2_ID)).willReturn(Optional.of(employee2));
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
				.andExpect(view().name("tasks/unassignedTasksList"));
	}

	////////////////////////////////////////////////////////////////////////////////
	// Tests: List uncomplete

	@WithMockUser(value = "spring")
	@Test
	void testListUncompleteTasks() throws Exception {
		mockMvc.perform(get("/tasks/uncompleteTasks"))
				// result
				.andExpect(status().isOk()).andExpect(model().attributeExists("selections"))
				.andExpect(view().name("tasks/uncompleteTasksList"));
	}

	////////////////////////////////////////////////////////////////////////////////
	// Tests: List mine

	@WithMockUser(value = "spring")
	@Test
	void testListMyTasks() throws Exception {
		mockMvc.perform(get("/myTasks"))
				// result
				.andExpect(status().isOk()).andExpect(model().attributeExists("selections"))
				.andExpect(view().name("tasks/myTasksList"));
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

	// TODO

	////////////////////////////////////////////////////////////////////////////////
	// Tests: Assign employee

	@WithMockUser(value = "spring")
	@Test
	void testInitAssignEmployee() throws Exception {
		mockMvc.perform(get("/tasks/{taskId}/assignEmployee", TASK_ID))
				// result
				.andExpect(status().isOk()).andExpect(model().attributeExists("selections"))
				.andExpect(view().name("tasks/selectEmployee"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testInitAssignEmployeeWrongTaskId() throws Exception {
		mockMvc.perform(get("/tasks/{taskId}/assignEmployee/", 0))
				// result
				.andExpect(status().isOk()).andExpect(model().attributeExists("error"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testInitAssignEmployeeTaskComplete() throws Exception {
		task.setComplete(true);
		mockMvc.perform(get("/tasks/{taskId}/assignEmployee", TASK_ID))
				// result
				.andExpect(status().isOk()).andExpect(model().attributeExists("error"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testAssignEmployeeSuccess() throws Exception {
		mockMvc.perform(post("/tasks/{taskId}/assignEmployee/{employeeId}", TASK_ID, EMPLOYEE_1_ID)
				// other
				.with(csrf()))
				// result
				.andExpect(status().isOk()).andExpect(model().attributeExists("selections"))
				.andExpect(view().name("tasks/uncompleteTasksList"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testAssignEmployeeWrongTaskId() throws Exception {
		mockMvc.perform(post("/tasks/{taskId}/assignEmployee/{employeeId}", 0, EMPLOYEE_1_ID)
				// other
				.with(csrf()))
				// result
				.andExpect(status().isOk()).andExpect(model().attributeExists("error"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testAssignEmployeeWrongEmployeeId() throws Exception {
		mockMvc.perform(post("/tasks/{taskId}/assignEmployee/{employeeId}", TASK_ID, 0)
				// other
				.with(csrf()))
				// result
				.andExpect(status().isOk()).andExpect(model().attributeExists("error"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testAssignEmployeeWithoutBuilding() throws Exception {
		employee1.setBuilding(null);
		mockMvc.perform(post("/tasks/{taskId}/assignEmployee/{employeeId}", TASK_ID, EMPLOYEE_1_ID)
				// other
				.with(csrf()))
				// result
				.andExpect(status().isOk()).andExpect(model().attributeExists("error"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testAssignEmployeeTaskComplete() throws Exception {
		task.setComplete(true);
		mockMvc.perform(post("/tasks/{taskId}/assignEmployee/{employeeId}", TASK_ID, EMPLOYEE_1_ID)
				// other
				.with(csrf()))
				// result
				.andExpect(status().isOk()).andExpect(model().attributeExists("error"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testAssignEmployeeAlreadyAssigned() throws Exception {
		employee1.addTask(task);
		task.addEmployee(employee1);
		mockMvc.perform(post("/tasks/{taskId}/assignEmployee/{employeeId}", TASK_ID, EMPLOYEE_1_ID)
				// other
				.with(csrf()))
				// result
				.andExpect(status().isOk()).andExpect(model().attributeExists("error"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testAssignEmployeeDifferentBuilding() throws Exception {
		employee1.addTask(task);
		task.addEmployee(employee1);
		mockMvc.perform(post("/tasks/{taskId}/assignEmployee/{employeeId}", TASK_ID, EMPLOYEE_2_ID)
				// other
				.with(csrf()))
				// result
				.andExpect(status().isOk()).andExpect(model().attributeExists("error"));
	}

	////////////////////////////////////////////////////////////////////////////////
	// Tests: Assign tool

	// TODO

}
