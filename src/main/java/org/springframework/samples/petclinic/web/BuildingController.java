package org.springframework.samples.petclinic.web;

import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Building;
import org.springframework.samples.petclinic.service.BuildingService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class BuildingController {

	////////////////////////////////////////////////////////////////////////////////
	// Services

	private final BuildingService buildingService;

	////////////////////////////////////////////////////////////////////////////////
	// Initializers

	@Autowired
	public BuildingController(BuildingService buildingService) {
		this.buildingService = buildingService;
	}

	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	////////////////////////////////////////////////////////////////////////////////
	// List

	@GetMapping(value = "/buildings")
	public ModelAndView list() {
		ModelAndView mav = new ModelAndView("buildings/buildingsList");

		Iterable<Building> allBuildings = this.buildingService.findAll();
		mav.addObject("selections", allBuildings);

		return mav;
	}

	////////////////////////////////////////////////////////////////////////////////
	// Show

	@GetMapping(value = "/buildings/{buildingId}")
	public ModelAndView show(@PathVariable("buildingId") int buildingId) {
		ModelAndView mav;

		Optional<Building> building = this.buildingService.findBuildingById(buildingId);
		if (!building.isPresent()) {
			mav = list();
			mav.addObject("error", "The building with id " + buildingId + " could not be found.");
		} else {
			mav = new ModelAndView("buildings/buildingDetails");
			mav.addObject("building", building.get());
		}

		return mav;
	}

	////////////////////////////////////////////////////////////////////////////////
	// Create

	@GetMapping(value = "/buildings/new")
	public String initCreationForm(Map<String, Object> model) {
		Building building = new Building();
		model.put("building", building);
		return "buildings/createOrUpdateBuildingForm";
	}

	@PostMapping(value = "/buildings/new")
	public String processCreationForm(@Valid Building building, BindingResult result) {
		if (result.hasErrors()) {
			return "buildings/createOrUpdateBuildingForm";
		} else {
			this.buildingService.saveBuilding(building);
			return "redirect:/buildings/" + building.getId();
		}
	}

}
