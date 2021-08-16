package org.springframework.samples.petclinic.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Building;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.service.BuildingService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Optional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(controllers = BuildingController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
class BuildingControllerTests {

	////////////////////////////////////////////////////////////////////////////////
	// Initialize

	private static final int BUILDING_OWNER_ID = 1;

	// @Autowired
	// private BuildingController buildingController;

	@MockBean
	private BuildingService buildingService;

	@Autowired
	private MockMvc mockMvc;

	private Building building;

	@BeforeEach
	void setup() {
		building = new Building();
		building.setId(BUILDING_OWNER_ID);
		building.setName("Building");
		building.setAddress("c/Building");
		building.setIncome(0);
		given(this.buildingService.findBuildingById(BUILDING_OWNER_ID)).willReturn(Optional.of(building));
	}

	////////////////////////////////////////////////////////////////////////////////
	// Tests

	@WithMockUser(value = "spring")
	@Test
	void testInitCreationForm() throws Exception {
		mockMvc.perform(get("/buildings/new"))
				// result
				.andExpect(status().isOk()).andExpect(model().attributeExists("building"))
				.andExpect(view().name("buildings/createOrUpdateBuildingForm"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessCreationFormSuccess() throws Exception {
		mockMvc.perform(post("/buildings/new")
				// params
				.param("name", "Building").param("address", "c/Building")
				// other
				.with(csrf()))
				// result
				.andExpect(status().is3xxRedirection());
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessCreationFormHasErrors() throws Exception {
		mockMvc.perform(post("/buildings/new")
				// params
				.param("name", "").param("address", "")
				// other
				.with(csrf()))
				// result
				.andExpect(status().isOk()).andExpect(model().attributeHasErrors("building"))
				.andExpect(model().attributeHasFieldErrors("building", "name"))
				.andExpect(model().attributeHasFieldErrors("building", "address"))
				.andExpect(view().name("buildings/createOrUpdateBuildingForm"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testShowBuilding() throws Exception {
		mockMvc.perform(get("/buildings/{buildingId}", BUILDING_OWNER_ID))
				// result
				.andExpect(status().isOk())
				.andExpect(model().attribute("building", hasProperty("name", is("Building"))))
				.andExpect(model().attribute("building", hasProperty("address", is("c/Building"))))
				.andExpect(view().name("buildings/buildingDetails"));
	}

}
