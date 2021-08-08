package org.springframework.samples.petclinic.web;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Task;
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

	////////////////////////////////////////////////////////////////////////////////
	// Initializers

	@Autowired
	public TaskController(TaskService taskService) {
		this.taskService = taskService;
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
		ModelAndView mav = new ModelAndView("myTasks/tasksList");

		// TODO: GET TASKS OF EMPLOYEE
		Iterable<Task> allTasks = null;
		mav.addObject("selections", allTasks);

		return mav;
	}

}
