package org.springframework.samples.petclinic.web;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Building;
import org.springframework.samples.petclinic.model.Employee;
import org.springframework.samples.petclinic.model.Task;
import org.springframework.samples.petclinic.model.Tool;
import org.springframework.samples.petclinic.service.BuildingService;
import org.springframework.samples.petclinic.service.EmployeeService;
import org.springframework.samples.petclinic.service.TaskService;
import org.springframework.samples.petclinic.service.ToolService;
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
	private final ToolService toolService;
	private final BuildingService buildingService;

	////////////////////////////////////////////////////////////////////////////////
	// Initializers

	@Autowired
	public TaskController(TaskService taskService, EmployeeService employeeService, ToolService toolService,
			BuildingService buildingService) {
		this.taskService = taskService;
		this.employeeService = employeeService;
		this.toolService = toolService;
		this.buildingService = buildingService;
	}

	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	////////////////////////////////////////////////////////////////////////////////
	// List (manager)

	@GetMapping(value = "/tasks/unassignedTasks")
	public ModelAndView listUnassignedManager() {
		ModelAndView mav = new ModelAndView("tasks/unassignedTasksList");

		Iterable<Task> tasks = this.taskService.findUnassigned();
		mav.addObject("selections", tasks);

		return mav;
	}

	@GetMapping(value = "/tasks/uncompleteTasks")
	public ModelAndView listUncompletedManager() {
		ModelAndView mav = new ModelAndView("tasks/uncompleteTasksList");

		Iterable<Task> tasks = this.taskService.findAssignedAndUncomplete();
		mav.addObject("selections", tasks);

		return mav;
	}

	////////////////////////////////////////////////////////////////////////////////
	// Show (manager)

	@GetMapping(value = "/tasks/{taskId}")
	public ModelAndView showManager(@PathVariable("taskId") int taskId) {
		ModelAndView mav;

		Optional<Task> task = this.taskService.findTaskById(taskId);
		if (!task.isPresent()) {
			mav = this.listUnassignedManager();
			mav.addObject("error", "The task with id " + taskId + " could not be found.");
		} else {
			mav = new ModelAndView("tasks/taskDetails");
			mav.addObject("task", task.get());
		}

		return mav;
	}

	////////////////////////////////////////////////////////////////////////////////
	// Create (manager)

	@GetMapping(value = "/tasks/new")
	public ModelAndView initCreationFormManager() {
		ModelAndView mav;

		mav = new ModelAndView("tasks/createOrUpdateTaskForm");
		Task task = new Task();
		mav.addObject("task", task);

		return mav;
	}

	@PostMapping(value = "/tasks/new")
	public ModelAndView processCreationFormManager(@Valid Task task, BindingResult result) {
		ModelAndView mav;

		if (result.hasErrors()) {
			mav = new ModelAndView("tasks/createOrUpdateTaskForm");
		} else {
			task.setComplete(false);
			this.taskService.saveTask(task);
			mav = new ModelAndView("redirect:/uncompleteTasks/" + task.getId());
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
			mav = new ModelAndView("welcome");
			mav.addObject("error", "The employee currently logged in could not be found.");
		} else {
			mav = new ModelAndView("tasks/myTasksList");
			Iterable<Task> tasks = this.taskService.findAssignedToEmployeeAndNotComplete(employee.get().getId());
			mav.addObject("selections", tasks);
		}

		return mav;
	}

	////////////////////////////////////////////////////////////////////////////////
	// Complete (employee)

	@PostMapping(value = "/myTasks/{taskId}/complete")
	public ModelAndView completeEmployee(@PathVariable("taskId") int taskId) {
		ModelAndView mav;

		Optional<Employee> employee = this.employeeService.findEmployeePrincipal();
		Optional<Task> task = this.taskService.findTaskById(taskId);

		if (!employee.isPresent()) {
			// No *debería* ser posible
			// El usuario necesita la autoridad "employee" para llegar aquí
			mav = new ModelAndView("welcome");
			mav.addObject("error", "The employee currently logged in could not be found.");
		} else if (!task.isPresent()) {
			mav = this.listEmployee();
			mav.addObject("error", "The task with id " + taskId + " could not be found.");
		} else if (!task.get().getEmployees().contains(employee.get())) {
			mav = this.listEmployee();
			mav.addObject("error", "The task with id " + taskId + " doesn't belong to the employee with id "
					+ employee.get().getId() + ".");
		} else if (task.get().getComplete()) {
			mav = this.listEmployee();
			mav.addObject("error", "The task with id " + taskId + " is already complete.");
		} else {
			Task taskInternal = task.get();
			for (Tool toolOfTask : taskInternal.getTools()) {
				toolOfTask.setTask(null);
				this.toolService.saveTool(toolOfTask);
				taskInternal.removeTool(toolOfTask);
			}
			taskInternal.setComplete(true);
			this.taskService.saveTask(taskInternal);
			Employee employeeOfTask = taskInternal.getEmployees().get(0);
			Building buildingOfTask = employeeOfTask.getBuilding();
			buildingOfTask.setIncome(buildingOfTask.getIncome() + taskInternal.getIncome());
			this.buildingService.saveBuilding(buildingOfTask);
			mav = this.listEmployee();
		}

		return mav;
	}

	////////////////////////////////////////////////////////////////////////////////
	// Assign employee (manager)

	@GetMapping(value = "/tasks/{taskId}/assignEmployee")
	public ModelAndView assignEmployeeListManager(@PathVariable("taskId") int taskId) {
		ModelAndView mav;

		Optional<Task> task = this.taskService.findTaskById(taskId);

		if (!task.isPresent()) {
			mav = this.listUnassignedManager();
			mav.addObject("error", "The task with id " + taskId + " could not be found.");
		} else if (task.get().getComplete()) {
			mav = this.listUnassignedManager();
			mav.addObject("error", "The task with id " + taskId + " is complete.");
		} else {
			mav = new ModelAndView("tasks/selectEmployee");
			Iterable<Employee> employees = this.employeeService.findAssignableToTask(taskId);

			mav.addObject("selections", employees);
			mav.addObject("taskId", taskId);
		}

		return mav;
	}

	@PostMapping(value = "/tasks/{taskId}/assignEmployee/{employeeId}")
	public ModelAndView assignEmployeeManager(@PathVariable("taskId") int taskId,
			@PathVariable("employeeId") int employeeId) {
		ModelAndView mav;

		Optional<Task> task = this.taskService.findTaskById(taskId);
		Optional<Employee> employee = this.employeeService.findEmployeeById(employeeId);

		if (!employee.isPresent()) {
			mav = this.listUnassignedManager();
			mav.addObject("error", "The employee with id " + employeeId + " could not be found.");
		} else if (!task.isPresent()) {
			mav = this.listUnassignedManager();
			mav.addObject("error", "The task with id " + taskId + " could not be found.");
		} else if (employee.get().getBuilding() == null) {
			mav = this.listUnassignedManager();
			mav.addObject("error", "The employee with id " + employeeId + " has no building.");
		} else if (task.get().getComplete()) {
			mav = this.listUnassignedManager();
			mav.addObject("error", "The task with id " + taskId + " is complete.");
		} else if (task.get().getEmployees().contains(employee.get())) {
			mav = this.listUnassignedManager();
			mav.addObject("error",
					"The task with id " + taskId + " already has the employee with id " + employeeId + ".");
		} else if (task.get().getEmployees().size() > 0
				? !task.get().getEmployees().get(0).getBuilding().equals(employee.get().getBuilding())
				: false) {
			mav = this.listUnassignedManager();
			mav.addObject("error",
					"The task with id " + taskId + " cannot be assigned to employees at different buildings.");
		} else {
			Task taskInternal = task.get();
			Employee employeeInternal = employee.get();
			taskInternal.addEmployee(employeeInternal);
			this.taskService.saveTask(taskInternal);
			employeeInternal.addTask(taskInternal);
			this.employeeService.saveEmployee(employeeInternal);
			mav = this.listUncompletedManager();
		}

		return mav;
	}

	////////////////////////////////////////////////////////////////////////////////
	// Assign tool (manager)

	@GetMapping(value = "/tasks/{taskId}/assignTool")
	public ModelAndView assignToolListManager(@PathVariable("taskId") int taskId) {
		ModelAndView mav;

		Optional<Task> task = this.taskService.findTaskById(taskId);

		if (!task.isPresent()) {
			mav = this.listUncompletedManager();
			mav.addObject("error", "The task with id " + taskId + " could not be found.");
		} else if (task.get().getComplete()) {
			mav = this.listUncompletedManager();
			mav.addObject("error", "The task with id " + taskId + " is complete.");
		} else if (task.get().getEmployees().size() == 0) {
			mav = this.listUncompletedManager();
			mav.addObject("error",
					"The task with id " + taskId + " cannot be assigned tools as it has no employees assigned yet.");
		} else {
			mav = new ModelAndView("tasks/selectTool");
			Iterable<Tool> tools = this.toolService.findAssignableToTask(taskId);
			mav.addObject("selections", tools);
			mav.addObject("taskId", taskId);
		}

		return mav;
	}

	@PostMapping(value = "/tasks/{taskId}/assignTool/{toolId}")
	public ModelAndView assignToolManager(@PathVariable("taskId") int taskId, @PathVariable("toolId") int toolId) {
		ModelAndView mav;

		Optional<Task> task = this.taskService.findTaskById(taskId);
		Optional<Tool> tool = this.toolService.findToolById(toolId);

		if (!tool.isPresent()) {
			mav = this.listUnassignedManager();
			mav.addObject("error", "The tool with id " + toolId + " could not be found.");
		} else if (tool.get().getTask() != null) {
			mav = this.listUnassignedManager();
			mav.addObject("error", "The tool with id " + toolId + " already has a task.");
		} else if (!task.isPresent()) {
			mav = this.listUnassignedManager();
			mav.addObject("error", "The task with id " + taskId + " could not be found.");
		} else if (task.get().getComplete()) {
			mav = this.listUnassignedManager();
			mav.addObject("error", "The task with id " + taskId + " is complete.");
		} else if (task.get().getEmployees().size() == 0) {
			mav = this.listUnassignedManager();
			mav.addObject("error",
					"The task with id " + taskId + " cannot be assigned tools as it has no employees assigned yet.");
		} else if (!task.get().getEmployees().get(0).getBuilding().equals(tool.get().getBuilding())) {
			mav = this.listUnassignedManager();
			mav.addObject("error",
					"The task with id " + taskId + " cannot be assigned tools and employees at different buildings.");
		} else {
			Task taskInternal = task.get();
			Tool toolInternal = tool.get();
			taskInternal.addTool(toolInternal);
			this.taskService.saveTask(taskInternal);
			toolInternal.setTask(taskInternal);
			this.toolService.saveTool(toolInternal);
			mav = this.listUncompletedManager();
		}

		return mav;
	}

}
