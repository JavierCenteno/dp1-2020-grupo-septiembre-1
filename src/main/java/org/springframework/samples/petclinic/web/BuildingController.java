package org.springframework.samples.petclinic.web;

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
	public ModelAndView initCreationForm() {
		ModelAndView mav;

		mav = new ModelAndView("buildings/createOrUpdateBuildingForm");
		Building building = new Building();
		mav.addObject("building", building);

		return mav;
	}

	@PostMapping(value = "/buildings/new")
	public ModelAndView processCreationForm(@Valid Building building, BindingResult result) {
		ModelAndView mav;

		if (result.hasErrors()) {
			mav = new ModelAndView("buildings/createOrUpdateBuildingForm");
		} else {
			building.setIncome(0);
			this.buildingService.saveBuilding(building);
			mav = new ModelAndView("redirect:/buildings/" + building.getId());
		}

		return mav;
	}

}
