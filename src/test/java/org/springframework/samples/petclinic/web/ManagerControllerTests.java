package org.springframework.samples.petclinic.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Manager;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.ManagerService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Optional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(controllers = ManagerController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
class ManagerControllerTests {

	////////////////////////////////////////////////////////////////////////////////
	// Initialize

	private static final int MANAGER_ID = 1;

	// @Autowired
	// private ManagerController managerController;

	@MockBean
	private ManagerService managerService;

	@MockBean
	private UserService userService;

	@MockBean
	private AuthoritiesService authoritiesService;

	@Autowired
	private MockMvc mockMvc;

	private Manager manager;

	@BeforeEach
	void setup() {
		manager = new Manager();
		manager.setId(MANAGER_ID);
		manager.setName("Manager");
		manager.setEmail("manager@manager.com");
		manager.setAddress("c/Manager");
		given(this.managerService.findManagerById(MANAGER_ID)).willReturn(Optional.of(manager));
	}

	////////////////////////////////////////////////////////////////////////////////
	// Tests: List

	@WithMockUser(value = "spring")
	@Test
	void testListManagers() throws Exception {
		mockMvc.perform(get("/managers"))
				// result
				.andExpect(status().isOk()).andExpect(model().attributeExists("selections"))
				.andExpect(view().name("managers/managersList"));
	}

	////////////////////////////////////////////////////////////////////////////////
	// Tests: Show

	@WithMockUser(value = "spring")
	@Test
	void testShowManager() throws Exception {
		mockMvc.perform(get("/managers/{managerId}", MANAGER_ID))
				// result
				.andExpect(status().isOk())
				.andExpect(model().attribute("manager", hasProperty("name", is("Manager"))))
				.andExpect(model().attribute("manager", hasProperty("email", is("manager@manager.com"))))
				.andExpect(model().attribute("manager", hasProperty("address", is("c/Manager"))))
				.andExpect(view().name("managers/managerDetails"));
	}

	////////////////////////////////////////////////////////////////////////////////
	// Tests: Create

	@WithMockUser(value = "spring")
	@Test
	void testInitCreationForm() throws Exception {
		mockMvc.perform(get("/managers/new"))
				// result
				.andExpect(status().isOk()).andExpect(model().attributeExists("manager"))
				.andExpect(view().name("managers/createOrUpdateManagerForm"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessCreationFormSuccess() throws Exception {
		mockMvc.perform(post("/managers/new")
				// params
				.param("name", "Manager").param("email", "manager@manager.com").param("address", "c/Manager").param("user.username", "manager").param("user.password", "manager")
				// other
				.with(csrf()))
				// result
				.andExpect(status().is3xxRedirection());
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessCreationFormHasErrors() throws Exception {
		mockMvc.perform(post("/managers/new")
				// params
				.param("name", "").param("email", "").param("address", "").param("user.username", "manager").param("user.password", "manager")
				// other
				.with(csrf()))
				// result
				.andExpect(status().isOk()).andExpect(model().attributeHasErrors("manager"))
				.andExpect(model().attributeHasFieldErrors("manager", "name"))
				.andExpect(model().attributeHasFieldErrors("manager", "email"))
				.andExpect(model().attributeHasFieldErrors("manager", "address"))
				.andExpect(view().name("managers/createOrUpdateManagerForm"));
	}

}
