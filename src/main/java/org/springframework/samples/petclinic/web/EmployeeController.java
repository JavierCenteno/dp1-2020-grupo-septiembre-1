package org.springframework.samples.petclinic.web;

import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Employee;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.EmployeeService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class EmployeeController {

	////////////////////////////////////////////////////////////////////////////////
	// URIs

	private static final String VIEWS_EMPLOYEE_CREATE_OR_UPDATE_FORM = "employee/createOrUpdateEmployeeForm";

	////////////////////////////////////////////////////////////////////////////////
	// Services

	private final EmployeeService employeeService;

	////////////////////////////////////////////////////////////////////////////////
	// Initializers

	@Autowired
	public EmployeeController(EmployeeService employeeService, UserService userService, AuthoritiesService authoritiesService) {
		this.employeeService = employeeService;
	}

	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	////////////////////////////////////////////////////////////////////////////////
	// Create

	@GetMapping(value = "/employees/new")
	public String initCreationForm(Map<String, Object> model) {
		Employee employee = new Employee();
		model.put("employee", employee);
		return VIEWS_EMPLOYEE_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/employees/new")
	public String processCreationForm(@Valid Employee employee, BindingResult result) {
		if (result.hasErrors()) {
			return VIEWS_EMPLOYEE_CREATE_OR_UPDATE_FORM;
		}
		else {
			this.employeeService.saveEmployee(employee);
			return "redirect:/employees/" + employee.getId();
		}
	}

	////////////////////////////////////////////////////////////////////////////////
	// Update

	@GetMapping(value = "/employees/{employeeId}/edit")
	public String initUpdateEmployeeForm(@PathVariable("employeeId") int employeeId, Model model) {
		Optional<Employee> optionalEmployee = this.employeeService.findEmployeeById(employeeId);
		// TODO: what if employee doesn't exist?
		Employee employee = optionalEmployee.get();
		model.addAttribute(employee);
		return VIEWS_EMPLOYEE_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/employees/{employeeId}/edit")
	public String processUpdateEmployeeForm(@Valid Employee employee, BindingResult result,
			@PathVariable("employeeId") int employeeId) {
		if (result.hasErrors()) {
			return VIEWS_EMPLOYEE_CREATE_OR_UPDATE_FORM;
		}
		else {
			employee.setId(employeeId);
			this.employeeService.saveEmployee(employee);
			return "redirect:/employees/{employeeId}";
		}
	}

	////////////////////////////////////////////////////////////////////////////////
	// Show

	@GetMapping("/employees/{employeeId}")
	public ModelAndView showEmployee(@PathVariable("employeeId") int employeeId) {
		ModelAndView mav = new ModelAndView("employees/employeeDetails");
		mav.addObject(this.employeeService.findEmployeeById(employeeId));
		return mav;
	}

	////////////////////////////////////////////////////////////////////////////////
	// List unassigned employees

	@GetMapping("/unassignedEmployees")
	public ModelAndView unassignedEmployees() {
		ModelAndView mav = new ModelAndView("employees/unassignedEmployeesList");
		Iterable<Employee> employeesNotAssignedToABuilding = this.employeeService.findNotAssignedToABuilding();
		mav.addObject("selections", employeesNotAssignedToABuilding);
		return mav;
	}

}
