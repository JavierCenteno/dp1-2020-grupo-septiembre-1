package org.springframework.samples.petclinic.web;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Employee;
import org.springframework.samples.petclinic.model.Manager;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.EmployeeService;
import org.springframework.samples.petclinic.service.ManagerService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {

	////////////////////////////////////////////////////////////////////////////////
	// URIs

	private static final String VIEWS_MANAGER_CREATE_FORM = "users/createManagerForm";
	private static final String VIEWS_EMPLOYEE_CREATE_FORM = "users/createEmployeeForm";

	////////////////////////////////////////////////////////////////////////////////
	// Services

	private final ManagerService managerService;
	private final EmployeeService employeeService;

	////////////////////////////////////////////////////////////////////////////////
	// Initializers

	@Autowired
	public UserController(ManagerService managerService, EmployeeService employeeService, UserService userService, AuthoritiesService authoritiesService) {
		this.managerService = managerService;
		this.employeeService = employeeService;
	}

	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	////////////////////////////////////////////////////////////////////////////////
	// Create manager

	@GetMapping(value = "/users/manager/new")
	public String initManagerCreationForm(Map<String, Object> model) {
		Manager manager = new Manager();
		model.put("manager", manager);
		return VIEWS_MANAGER_CREATE_FORM;
	}

	@PostMapping(value = "/users/manager/new")
	public String processManagerCreationForm(@Valid Manager manager, BindingResult result) {
		if (result.hasErrors()) {
			return VIEWS_MANAGER_CREATE_FORM;
		}
		else {
			this.managerService.saveManager(manager);
			return "redirect:/";
		}
	}

	////////////////////////////////////////////////////////////////////////////////
	// Create employee

	@GetMapping(value = "/users/employee/new")
	public String initEmployeeCreationForm(Map<String, Object> model) {
		Employee employee = new Employee();
		model.put("employee", employee);
		return VIEWS_EMPLOYEE_CREATE_FORM;
	}

	@PostMapping(value = "/users/employee/new")
	public String processEmployeeCreationForm(@Valid Employee employee, BindingResult result) {
		if (result.hasErrors()) {
			return VIEWS_EMPLOYEE_CREATE_FORM;
		}
		else {
			this.employeeService.saveEmployee(employee);
			return "redirect:/";
		}
	}

}
