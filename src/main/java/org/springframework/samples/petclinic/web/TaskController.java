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
	// List

	@GetMapping(value = "/tasks")
	public ModelAndView list() {
		ModelAndView mav = new ModelAndView("tasks/tasksList");

		Iterable<Task> allTasks = this.taskService.findAll();
		mav.addObject("selections", allTasks);

		return mav;
	}

	////////////////////////////////////////////////////////////////////////////////
	// Show

	@GetMapping(value = "/tasks/{taskId}")
	public ModelAndView show(@PathVariable("taskId") int taskId) {
		ModelAndView mav;

		Optional<Task> task = this.taskService.findTaskById(taskId);
		if (!task.isPresent()) {
			mav = this.list();
			mav.addObject("error", "The task with id " + taskId + " could not be found.");
		} else {
			mav = new ModelAndView("tasks/taskDetails");
			mav.addObject("task", task.get());
		}

		return mav;
	}

	////////////////////////////////////////////////////////////////////////////////
	// Create

	@GetMapping(value = "/tasks/new")
	public ModelAndView initCreationForm() {
		ModelAndView mav;

		mav = new ModelAndView("tasks/createOrUpdateTaskForm");
		Task task = new Task();
		mav.addObject("task", task);

		return mav;
	}

	@PostMapping(value = "/tasks/new")
	public ModelAndView processCreationForm(@Valid Task task, BindingResult result) {
		ModelAndView mav;

		if (result.hasErrors()) {
			mav = new ModelAndView("tasks/createOrUpdateTaskForm");
		} else {
			this.taskService.saveTask(task);
			mav = new ModelAndView("redirect:/tasks/" + task.getId());
		}

		return mav;
	}

}
