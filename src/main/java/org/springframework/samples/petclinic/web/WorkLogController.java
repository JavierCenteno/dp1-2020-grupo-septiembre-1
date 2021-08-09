package org.springframework.samples.petclinic.web;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.WorkLog;
import org.springframework.samples.petclinic.service.EmployeeService;
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
	private final EmployeeService employeeService;

	////////////////////////////////////////////////////////////////////////////////
	// Initializers

	@Autowired
	public WorkLogController(WorkLogService workLogService, EmployeeService employeeService) {
		this.workLogService = workLogService;
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

		// TODO:
		// CHECK TOTAL HOURS IN WORKLOGS FOR DATE DON'T EXCEED 8
		// CHECK THAT THE PRINCIPAL EMPLOYEE IS ASSIGNED TO THE TASK
		// CHECK THAT THE TASK ISN'T ALREADY COMPLETE

		if (result.hasErrors()) {
			mav = new ModelAndView("tasks/createOrUpdateWorkLogForm");
		} else {
			this.workLogService.saveWorkLog(workLog);
			mav = new ModelAndView("redirect:/myTasks/");
		}

		return mav;
	}

}
