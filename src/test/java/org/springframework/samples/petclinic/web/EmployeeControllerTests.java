package org.springframework.samples.petclinic.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Building;
import org.springframework.samples.petclinic.model.Employee;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.BuildingService;
import org.springframework.samples.petclinic.service.EmployeeService;
import org.springframework.samples.petclinic.service.TaskService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Optional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(controllers = EmployeeController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
class EmployeeControllerTests {

	////////////////////////////////////////////////////////////////////////////////
	// Initialize

	private static final int EMPLOYEE_ID = 1;

	private static final int BUILDING_1_ID = 1;

	private static final int BUILDING_2_ID = 2;

	// @Autowired
	// private EmployeeController employeeController;

	@MockBean
	private EmployeeService employeeService;

	@MockBean
	private UserService userService;

	@MockBean
	private AuthoritiesService authoritiesService;

	@MockBean
	private BuildingService buildingService;

	@MockBean
	private TaskService taskService;

	@Autowired
	private MockMvc mockMvc;

	private Employee employee;

	private Building building1;

	private Building building2;

	@BeforeEach
	void setup() {
		employee = new Employee();
		employee.setId(EMPLOYEE_ID);
		employee.setName("Employee");
		employee.setEmail("employee@employee.com");
		employee.setAddress("c/Employee");
		given(this.employeeService.findEmployeeById(EMPLOYEE_ID)).willReturn(Optional.of(employee));
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
	}

	////////////////////////////////////////////////////////////////////////////////
	// Tests: List

	@WithMockUser(value = "spring")
	@Test
	void testListEmployees() throws Exception {
		mockMvc.perform(get("/employees"))
				// result
				.andExpect(status().isOk()).andExpect(model().attributeExists("selections"))
				.andExpect(view().name("employees/employeesList"));
	}

	////////////////////////////////////////////////////////////////////////////////
	// Tests: Show

	@WithMockUser(value = "spring")
	@Test
	void testShowEmployee() throws Exception {
		mockMvc.perform(get("/employees/{employeeId}", EMPLOYEE_ID))
				// result
				.andExpect(status().isOk())
				.andExpect(model().attribute("employee", hasProperty("name", is("Employee"))))
				.andExpect(model().attribute("employee", hasProperty("email", is("employee@employee.com"))))
				.andExpect(model().attribute("employee", hasProperty("address", is("c/Employee"))))
				.andExpect(view().name("employees/employeeDetails"));
	}

	////////////////////////////////////////////////////////////////////////////////
	// Tests: Create

	@WithMockUser(value = "spring")
	@Test
	void testInitCreationForm() throws Exception {
		mockMvc.perform(get("/employees/new"))
				// result
				.andExpect(status().isOk()).andExpect(model().attributeExists("employee"))
				.andExpect(view().name("employees/createOrUpdateEmployeeForm"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessCreationFormSuccess() throws Exception {
		mockMvc.perform(post("/employees/new")
				// params
				.param("name", "Employee").param("email", "employee@employee.com").param("address", "c/Employee")
				// other
				.with(csrf()))
				// result
				.andExpect(status().is3xxRedirection());
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessCreationFormHasErrors() throws Exception {
		mockMvc.perform(post("/employees/new")
				// params
				.param("name", "").param("email", "").param("address", "")
				// other
				.with(csrf()))
				// result
				.andExpect(status().isOk()).andExpect(model().attributeHasErrors("employee"))
				.andExpect(model().attributeHasFieldErrors("employee", "name"))
				.andExpect(model().attributeHasFieldErrors("employee", "email"))
				.andExpect(model().attributeHasFieldErrors("employee", "address"))
				.andExpect(view().name("employees/createOrUpdateEmployeeForm"));
	}

	////////////////////////////////////////////////////////////////////////////////
	// Tests: List unassigned

	@WithMockUser(value = "spring")
	@Test
	void testListUnassignedEmployees() throws Exception {
		mockMvc.perform(get("/unassignedEmployees"))
				// result
				.andExpect(status().isOk()).andExpect(model().attributeExists("selections"))
				.andExpect(view().name("employees/unassignedEmployeesList"));
	}

	////////////////////////////////////////////////////////////////////////////////
	// Tests: Assign building

	@WithMockUser(value = "spring")
	@Test
	void testInitAssignBuilding() throws Exception {
		mockMvc.perform(get("/unassignedEmployees/{employeeId}/assignBuilding", EMPLOYEE_ID))
				// result
				.andExpect(status().isOk()).andExpect(model().attributeExists("selections"))
				.andExpect(view().name("employees/selectBuilding"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testInitAssignBuildingWrongEmployeeId() throws Exception {
		mockMvc.perform(get("/unassignedEmployees/{employeeId}/assignBuilding", 0))
				// result
				.andExpect(status().isOk()).andExpect(model().attributeExists("error"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testAssignBuildingSuccess() throws Exception {
		mockMvc.perform(post("/unassignedEmployees/{employeeId}/assignBuilding/{buildingId}", EMPLOYEE_ID, BUILDING_1_ID)
				// other
				.with(csrf()))
				// result
				.andExpect(status().isOk()).andExpect(model().attributeExists("selections"))
				.andExpect(view().name("employees/unassignedEmployeesList"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testAssignBuildingWrongEmployeeId() throws Exception {
		mockMvc.perform(post("/unassignedEmployees/{employeeId}/assignBuilding/{buildingId}", 0, BUILDING_1_ID)
				// other
				.with(csrf()))
				// result
				.andExpect(status().isOk()).andExpect(model().attributeExists("error"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testAssignBuildingWrongBuildingId() throws Exception {
		mockMvc.perform(post("/unassignedEmployees/{employeeId}/assignBuilding/{buildingId}", EMPLOYEE_ID, 0)
				// other
				.with(csrf()))
				// result
				.andExpect(status().isOk()).andExpect(model().attributeExists("error"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testAssignBuildingWhenEmployeeAlreadyHasBuilding() throws Exception {
		employee.setBuilding(building1);
		mockMvc.perform(post("/unassignedEmployees/{employeeId}/assignBuilding/{buildingId}", EMPLOYEE_ID, BUILDING_2_ID)
				// other
				.with(csrf()))
				// result
				.andExpect(status().isOk()).andExpect(model().attributeExists("error"));
	}

}
