package org.springframework.samples.petclinic.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.service.BuildingService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

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
	// Create

}
