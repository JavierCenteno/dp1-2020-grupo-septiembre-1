package org.springframework.samples.petclinic.web;

import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Building;
import org.springframework.samples.petclinic.model.Employee;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.BuildingService;
import org.springframework.samples.petclinic.service.EmployeeService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class EmployeeController {

	////////////////////////////////////////////////////////////////////////////////
	// Services

	private final EmployeeService employeeService;
	private final BuildingService buildingService;

	////////////////////////////////////////////////////////////////////////////////
	// Initializers

	@Autowired
	public EmployeeController(EmployeeService employeeService, UserService userService,
			AuthoritiesService authoritiesService, BuildingService buildingService) {
		this.employeeService = employeeService;
		this.buildingService = buildingService;
	}

	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	////////////////////////////////////////////////////////////////////////////////
	// Create

	@GetMapping(value = "/employees/new")
	public ModelAndView initCreationForm(Map<String, Object> model) {
		ModelAndView mav;

		mav = new ModelAndView("employees/createOrUpdateEmployeeForm");
		Employee employee = new Employee();
		model.put("employee", employee);
		
		return mav;
	}

	@PostMapping(value = "/employees/new")
	public ModelAndView processCreationForm(@Valid Employee employee, BindingResult result) {
		ModelAndView mav;
		
		if (this.employeeService.findEmployeeByUsername(employee.getUser().getUsername()).isPresent()) {
			mav = new ModelAndView("welcome");
			mav.addObject("error", "There already exists an employee with the username \"" + employee.getUser().getUsername() + "\".");
		} else if (result.hasErrors()) {
			mav = new ModelAndView("employees/createOrUpdateEmployeeForm");
		} else {
			employee.getUser().setPassword(UserService.PASSWORD_ENCODER.encode(employee.getUser().getPassword()));
			this.employeeService.saveEmployee(employee);
			mav = new ModelAndView("redirect:/employees/" + employee.getId());
		}

		return mav;
	}

	////////////////////////////////////////////////////////////////////////////////
	// Update

	/*
	@GetMapping(value = "/employees/{employeeId}/edit")
	public String initUpdateEmployeeForm(@PathVariable("employeeId") int employeeId, Model model) {
		Optional<Employee> optionalEmployee = this.employeeService.findEmployeeById(employeeId);
		// TO DO: what if employee doesn't exist?
		Employee employee = optionalEmployee.get();
		model.addAttribute(employee);
		return VIEWS_EMPLOYEE_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/employees/{employeeId}/edit")
	public String processUpdateEmployeeForm(@Valid Employee employee, BindingResult result,
			@PathVariable("employeeId") int employeeId) {
		if (result.hasErrors()) {
			return VIEWS_EMPLOYEE_CREATE_OR_UPDATE_FORM;
		} else {
			employee.setId(employeeId);
			this.employeeService.saveEmployee(employee);
			return "redirect:/employees/{employeeId}";
		}
	}
	*/

	////////////////////////////////////////////////////////////////////////////////
	// Show

	@GetMapping(value = "/employees/{employeeId}")
	public ModelAndView showEmployee(@PathVariable("employeeId") int employeeId) {
		ModelAndView mav;

		Optional<Employee> employee = this.employeeService.findEmployeeById(employeeId);
		if (!employee.isPresent()) {
			mav = list();
			mav.addObject("error", "The employee with id " + employeeId + " could not be found.");
		} else {
			mav = new ModelAndView("employees/employeeDetails");
			mav.addObject("employee", employee.get());
		}

		return mav;
	}

	////////////////////////////////////////////////////////////////////////////////
	// List

	@GetMapping(value = "/employees")
	public ModelAndView list() {
		ModelAndView mav = new ModelAndView("employees/employeesList");

		Iterable<Employee> allEmployees = this.employeeService.findAll();
		mav.addObject("selections", allEmployees);

		return mav;
	}

	////////////////////////////////////////////////////////////////////////////////
	// List unassigned employees

	@GetMapping(value = "/unassignedEmployees")
	public ModelAndView unassignedEmployees() {
		ModelAndView mav = new ModelAndView("employees/unassignedEmployeesList");
		Iterable<Employee> employeesNotAssignedToABuilding = this.employeeService.findNotAssignedToABuilding();
		mav.addObject("selections", employeesNotAssignedToABuilding);
		return mav;
	}

	@GetMapping(value = "/unassignedEmployees/{employeeId}/assignBuilding")
	public ModelAndView unassignedEmployeesAssignBuildingList(@PathVariable("employeeId") int employeeId) {
		ModelAndView mav;

		Optional<Employee> employee = this.employeeService.findEmployeeById(employeeId);

		if (!employee.isPresent()) {
			mav = this.unassignedEmployees();
			mav.addObject("error", "The employee with id " + employeeId + " could not be found.");
		} else {
			mav = new ModelAndView("employees/selectBuilding");
			Iterable<Building> buildings = this.buildingService.findAll();
			mav.addObject("selections", buildings);
			mav.addObject("employeeId", employeeId);
		}

		return mav;
	}

	@PostMapping(value = "/unassignedEmployees/{employeeId}/assignBuilding/{buildingId}")
	public ModelAndView unassignedEmployeesAssignBuilding(@PathVariable("employeeId") int employeeId,
			@PathVariable("buildingId") int buildingId) {
		ModelAndView mav;

		Optional<Employee> employee = this.employeeService.findEmployeeById(employeeId);
		Optional<Building> building = this.buildingService.findBuildingById(buildingId);

		if (!employee.isPresent()) {
			mav = this.unassignedEmployees();
			mav.addObject("error", "The employee with id " + employeeId + " could not be found.");
		} else if (!building.isPresent()) {
			mav = this.unassignedEmployees();
			mav.addObject("error", "The building with id " + buildingId + " could not be found.");
		} else if (employee.get().getBuilding() != null) {
			mav = this.unassignedEmployees();
			mav.addObject("error", "The employee with id " + employeeId + " already has a building.");
		} else {
			Employee employeeInternal = employee.get();
			Building buildingInternal = building.get();
			buildingInternal.addEmployee(employeeInternal);
			this.buildingService.saveBuilding(buildingInternal);
			employeeInternal.setBuilding(buildingInternal);
			this.employeeService.saveEmployee(employeeInternal);
			mav = this.unassignedEmployees();
		}

		return mav;
	}

}
