package org.springframework.samples.petclinic.web;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Employee;
import org.springframework.samples.petclinic.model.Task;
import org.springframework.samples.petclinic.service.EmployeeService;
import org.springframework.samples.petclinic.service.TaskService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TaskController {

	////////////////////////////////////////////////////////////////////////////////
	// Services

	private final TaskService taskService;
	private final EmployeeService employeeService;

	////////////////////////////////////////////////////////////////////////////////
	// Initializers

	@Autowired
	public TaskController(TaskService taskService, EmployeeService employeeService) {
		this.taskService = taskService;
		this.employeeService = employeeService;
	}

	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	////////////////////////////////////////////////////////////////////////////////
	// List (manager)

	@GetMapping(value = "/allTasks")
	public ModelAndView listManager() {
		ModelAndView mav = new ModelAndView("allTasks/tasksList");

		Iterable<Task> allTasks = this.taskService.findUnassigned();
		mav.addObject("selections", allTasks);

		return mav;
	}

	////////////////////////////////////////////////////////////////////////////////
	// Show (manager)

	@GetMapping(value = "/allTasks/{taskId}")
	public ModelAndView showManager(@PathVariable("taskId") int taskId) {
		ModelAndView mav;

		Optional<Task> task = this.taskService.findTaskById(taskId);
		if (!task.isPresent()) {
			mav = this.listManager();
			mav.addObject("error", "The task with id " + taskId + " could not be found.");
		} else {
			mav = new ModelAndView("allTasks/taskDetails");
			mav.addObject("task", task.get());
		}

		return mav;
	}

	////////////////////////////////////////////////////////////////////////////////
	// Create (manager)

	@GetMapping(value = "/allTasks/new")
	public ModelAndView initCreationFormManager() {
		ModelAndView mav;

		mav = new ModelAndView("allTasks/createOrUpdateTaskForm");
		Task task = new Task();
		mav.addObject("task", task);

		return mav;
	}

	@PostMapping(value = "/allTasks/new")
	public ModelAndView processCreationFormManager(@Valid Task task, BindingResult result) {
		ModelAndView mav;

		if (result.hasErrors()) {
			mav = new ModelAndView("allTasks/createOrUpdateTaskForm");
		} else {
			this.taskService.saveTask(task);
			mav = new ModelAndView("redirect:/allTasks/" + task.getId());
		}

		return mav;
	}

	////////////////////////////////////////////////////////////////////////////////
	// List (employee)

	@GetMapping(value = "/myTasks")
	public ModelAndView listEmployee() {
		ModelAndView mav;

		Optional<Employee> employee = this.employeeService.findEmployeePrincipal();
		if (!employee.isPresent()) {
			// No *debería* ser posible
			// El usuario necesita la autoridad "employee" para llegar aquí
			mav = new ModelAndView("redirect:/");
		} else {
			mav = new ModelAndView("myTasks/tasksList");
			Iterable<Task> allTasks = this.taskService.findAssignedToEmployeeAndNotComplete(employee.get().getId());
			mav.addObject("selections", allTasks);
		}

		return mav;
	}

}
