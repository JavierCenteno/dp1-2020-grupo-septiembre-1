package org.springframework.samples.petclinic.web;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Building;
import org.springframework.samples.petclinic.model.Tool;
import org.springframework.samples.petclinic.service.BuildingService;
import org.springframework.samples.petclinic.service.ToolService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ToolController {

	////////////////////////////////////////////////////////////////////////////////
	// Services

	private final ToolService toolService;
	private final BuildingService buildingService;

	////////////////////////////////////////////////////////////////////////////////
	// Initializers

	@Autowired
	public ToolController(ToolService toolService, BuildingService buildingService) {
		this.toolService = toolService;
		this.buildingService = buildingService;
	}

	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	////////////////////////////////////////////////////////////////////////////////
	// List

	@GetMapping(value = "/buildings/{buildingId}/tools")
	public ModelAndView list(@PathVariable("buildingId") int buildingId) {
		ModelAndView mav;

		Optional<Building> building = this.buildingService.findBuildingById(buildingId);
		if (!building.isPresent()) {
			mav = new ModelAndView("welcome");
			mav.addObject("error", "The building with id " + buildingId + " could not be found.");
		} else {
			mav = new ModelAndView("tools/toolsList");
			Iterable<Tool> allTools = this.toolService.findByBuildingId(buildingId);
			mav.addObject("selections", allTools);
			mav.addObject("buildingId", buildingId);
		}

		return mav;
	}

	////////////////////////////////////////////////////////////////////////////////
	// Show

	@GetMapping(value = "/buildings/{buildingId}/tools/{toolId}")
	public ModelAndView show(@PathVariable("buildingId") int buildingId, @PathVariable("toolId") int toolId) {
		ModelAndView mav;

		Optional<Tool> tool = this.toolService.findToolById(toolId);
		Optional<Building> building = this.buildingService.findBuildingById(buildingId);
		if (!building.isPresent()) {
			mav = new ModelAndView("welcome");
			mav.addObject("error", "The building with id " + buildingId + " could not be found.");
		} else if (!tool.isPresent()) {
			mav = this.list(buildingId);
			mav.addObject("error", "The tool with id " + toolId + " could not be found.");
		} else if (tool.get().getBuilding().getId() != buildingId) {
			mav = this.list(buildingId);
			mav.addObject("error",
					"The tool with id " + toolId + " doesn't belong to the building with id " + buildingId + ".");
		} else {
			mav = new ModelAndView("tools/toolDetails");
			mav.addObject("tool", tool.get());
		}

		return mav;
	}

	////////////////////////////////////////////////////////////////////////////////
	// Create

	@GetMapping(value = "/buildings/{buildingId}/tools/new")
	public ModelAndView initCreationForm(@PathVariable("buildingId") int buildingId) {
		ModelAndView mav;

		Optional<Building> building = this.buildingService.findBuildingById(buildingId);
		if (!building.isPresent()) {
			mav = new ModelAndView("welcome");
			mav.addObject("error", "The building with id " + buildingId + " could not be found.");
		} else {
			mav = new ModelAndView("tools/createOrUpdateToolForm");
			Tool tool = new Tool();
			mav.addObject("tool", tool);
			mav.addObject("buildingId", buildingId);
		}

		return mav;
	}

	@PostMapping(value = "/buildings/{buildingId}/tools/new")
	public ModelAndView processCreationForm(@PathVariable("buildingId") int buildingId, @Valid Tool tool,
			BindingResult result) {
		ModelAndView mav;

		Optional<Building> building = this.buildingService.findBuildingById(buildingId);
		if (!building.isPresent()) {
			mav = new ModelAndView("welcome");
			mav.addObject("error", "The building with id " + buildingId + " could not be found.");
		} else if (result.hasErrors()) {
			mav = new ModelAndView("tools/createOrUpdateToolForm");
		} else {
			Building buildingInternal = building.get();
			tool.setBuilding(buildingInternal);
			this.toolService.saveTool(tool);
			buildingInternal.addTool(tool);
			this.buildingService.saveBuilding(buildingInternal);
			mav = new ModelAndView("redirect:/buildings/" + buildingId + "/tools/" + tool.getId());
		}

		return mav;
	}

}
