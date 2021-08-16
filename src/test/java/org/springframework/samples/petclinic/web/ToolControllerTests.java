package org.springframework.samples.petclinic.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Building;
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
import org.springframework.samples.petclinic.service.ToolService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Optional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(controllers = ToolController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
class ToolControllerTests {

	////////////////////////////////////////////////////////////////////////////////
	// Initialize

	private static final int BUILDING_ID = 1;
	private static final int TOOL_ID = 1;

	// @Autowired
	// private ToolController toolController;

	@MockBean
	private BuildingService buildingService;
	@MockBean
	private ToolService toolService;

	@Autowired
	private MockMvc mockMvc;

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
		given(this.toolService.findToolById(TOOL_ID)).willReturn(Optional.of(tool));
	}

	////////////////////////////////////////////////////////////////////////////////
	// Tests: List

	@WithMockUser(value = "spring")
	@Test
	void testListTools() throws Exception {
		mockMvc.perform(get("/buildings/{buildingId}/tools", BUILDING_ID))
				// result
				.andExpect(status().isOk()).andExpect(model().attributeExists("selections"))
				.andExpect(view().name("tools/toolsList"));
	}

	////////////////////////////////////////////////////////////////////////////////
	// Tests: Show

	@WithMockUser(value = "spring")
	@Test
	void testShowTool() throws Exception {
		mockMvc.perform(get("/buildings/{buildingId}/tools/{toolId}", BUILDING_ID, TOOL_ID))
				// result
				.andExpect(status().isOk()).andExpect(model().attribute("tool", hasProperty("name", is("Tool"))))
				.andExpect(view().name("tools/toolDetails"));
	}

	////////////////////////////////////////////////////////////////////////////////
	// Tests: Create

	@WithMockUser(value = "spring")
	@Test
	void testInitCreationForm() throws Exception {
		mockMvc.perform(get("/buildings/{buildingId}/tools/new", BUILDING_ID))
				// result
				.andExpect(status().isOk()).andExpect(model().attributeExists("tool"))
				.andExpect(view().name("tools/createOrUpdateToolForm"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessCreationFormSuccess() throws Exception {
		mockMvc.perform(post("/buildings/{buildingId}/tools/new", BUILDING_ID)
				// params
				.param("name", "Tool")
				// other
				.with(csrf()))
				// result
				.andExpect(status().is3xxRedirection());
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessCreationFormHasErrors() throws Exception {
		mockMvc.perform(post("/buildings/{buildingId}/tools/new", BUILDING_ID)
				// params
				.param("name", "")
				// other
				.with(csrf()))
				// result
				.andExpect(status().isOk()).andExpect(model().attributeHasErrors("tool"))
				.andExpect(model().attributeHasFieldErrors("tool", "name"))
				.andExpect(view().name("tools/createOrUpdateToolForm"));
	}

}
