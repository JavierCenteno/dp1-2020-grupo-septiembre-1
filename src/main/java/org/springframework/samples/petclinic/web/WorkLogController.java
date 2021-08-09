package org.springframework.samples.petclinic.web;

import java.util.Date;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Employee;
import org.springframework.samples.petclinic.model.Task;
import org.springframework.samples.petclinic.model.WorkLog;
import org.springframework.samples.petclinic.service.EmployeeService;
import org.springframework.samples.petclinic.service.TaskService;
import org.springframework.samples.petclinic.service.WorkLogService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class WorkLogController {

	////////////////////////////////////////////////////////////////////////////////
	// Services

	private final WorkLogService workLogService;
	private final TaskService taskService;
	private final TaskController taskController;
	private final EmployeeService employeeService;

	////////////////////////////////////////////////////////////////////////////////
	// Initializers

	@Autowired
	public WorkLogController(WorkLogService workLogService, TaskService taskService, TaskController taskController,
			EmployeeService employeeService) {
		this.workLogService = workLogService;
		this.taskService = taskService;
		this.taskController = taskController;
		this.employeeService = employeeService;
	}

	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	////////////////////////////////////////////////////////////////////////////////
	// Create

	@GetMapping(value = "/myTasks/{taskId}/workLog")
	public ModelAndView initCreationFormManager(@PathVariable("taskId") int taskId) {
		ModelAndView mav;

		mav = new ModelAndView("workLogs/createOrUpdateWorkLogForm");
		WorkLog workLog = new WorkLog();
		mav.addObject("workLog", workLog);

		return mav;
	}

	@PostMapping(value = "/myTasks/{taskId}/workLog")
	public ModelAndView processCreationFormManager(@PathVariable("taskId") int taskId, @Valid WorkLog workLog,
			BindingResult result) {
		ModelAndView mav;

		Optional<Employee> employee = this.employeeService.findEmployeePrincipal();
		Optional<Task> task = this.taskService.findTaskById(taskId);
		Integer totalHours = this.workLogService.findHoursLoggedIntoTaskByEmployeeAtDate(taskId, employee.get().getId(),
				new Date());
		if (!employee.isPresent()) {
			// No *debería* ser posible
			// El usuario necesita la autoridad "employee" para llegar aquí
			mav = new ModelAndView("redirect:/");
		} else if (!task.isPresent()) {
			mav = this.taskController.listEmployee();
			mav.addObject("error", "The task with id " + taskId + " could not be found.");
		} else if (!task.get().getEmployees().contains(employee.get())) {
			mav = this.taskController.listEmployee();
			mav.addObject("error", "The task with id " + taskId + " is not assigned to the employee.");
		} else if (task.get().getComplete()) {
			mav = this.taskController.listEmployee();
			mav.addObject("error", "The task with id " + taskId + " is already complete.");
		} else if (result.hasErrors()) {
			mav = new ModelAndView("tasks/createOrUpdateWorkLogForm");
		} else if (totalHours != null ? totalHours + workLog.getHours() > 8 : false) {
			mav = this.taskController.listEmployee();
			mav.addObject("error", "The task with id " + taskId + " can't have more than 8 hours logged in (currently: "
					+ totalHours + ", you attempted to add " + workLog.getHours() + ").");
		} else {
			this.workLogService.saveWorkLog(workLog);
			Task taskInternal = task.get();
			taskInternal.addWorkLog(workLog);
			this.taskService.saveTask(taskInternal);
			mav = new ModelAndView("redirect:/myTasks/");
		}

		return mav;
	}

}
