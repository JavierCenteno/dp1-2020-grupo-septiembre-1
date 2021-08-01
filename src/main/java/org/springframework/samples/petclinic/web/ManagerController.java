package org.springframework.samples.petclinic.web;

import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Manager;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.ManagerService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ManagerController {

	////////////////////////////////////////////////////////////////////////////////
	// URIs

	private static final String VIEWS_EMPLOYEE_CREATE_OR_UPDATE_FORM = "manager/createOrUpdateManagerForm";

	////////////////////////////////////////////////////////////////////////////////
	// Services

	private final ManagerService managerService;

	////////////////////////////////////////////////////////////////////////////////
	// Initializers

	@Autowired
	public ManagerController(ManagerService managerService, UserService userService, AuthoritiesService authoritiesService) {
		this.managerService = managerService;
	}

	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	////////////////////////////////////////////////////////////////////////////////
	// Create

	@GetMapping(value = "/managers/new")
	public String initCreationForm(Map<String, Object> model) {
		Manager manager = new Manager();
		model.put("manager", manager);
		return VIEWS_EMPLOYEE_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/managers/new")
	public String processCreationForm(@Valid Manager manager, BindingResult result) {
		if (result.hasErrors()) {
			return VIEWS_EMPLOYEE_CREATE_OR_UPDATE_FORM;
		}
		else {
			this.managerService.saveManager(manager);
			return "redirect:/managers/" + manager.getId();
		}
	}

	////////////////////////////////////////////////////////////////////////////////
	// Update

	@GetMapping(value = "/managers/{managerId}/edit")
	public String initUpdateManagerForm(@PathVariable("managerId") int managerId, Model model) {
		Optional<Manager> optionalManager = this.managerService.findManagerById(managerId);
		// TODO: what if manager doesn't exist?
		Manager manager = optionalManager.get();
		model.addAttribute(manager);
		return VIEWS_EMPLOYEE_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/managers/{managerId}/edit")
	public String processUpdateManagerForm(@Valid Manager manager, BindingResult result,
			@PathVariable("managerId") int managerId) {
		if (result.hasErrors()) {
			return VIEWS_EMPLOYEE_CREATE_OR_UPDATE_FORM;
		}
		else {
			manager.setId(managerId);
			this.managerService.saveManager(manager);
			return "redirect:/managers/{managerId}";
		}
	}

	////////////////////////////////////////////////////////////////////////////////
	// Show

	@GetMapping("/managers/{managerId}")
	public ModelAndView showManager(@PathVariable("managerId") int managerId) {
		ModelAndView mav = new ModelAndView("managers/managerDetails");
		mav.addObject(this.managerService.findManagerById(managerId));
		return mav;
	}

}
