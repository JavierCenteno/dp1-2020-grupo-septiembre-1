package org.springframework.samples.petclinic.service;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Employee;
import org.springframework.samples.petclinic.model.Task;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.repository.EmployeeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmployeeService {

	////////////////////////////////////////////////////////////////////////////////
	// Repository

	private EmployeeRepository employeeRepository;

	////////////////////////////////////////////////////////////////////////////////
	// Services

	private UserService userService;

	private AuthoritiesService authoritiesService;

	private TaskService taskService;

	////////////////////////////////////////////////////////////////////////////////
	// Constructor

	@Autowired
	public EmployeeService(EmployeeRepository employeeRepository, UserService userService,
			AuthoritiesService authoritiesService, TaskService taskService) {
		this.employeeRepository = employeeRepository;
		this.userService = userService;
		this.authoritiesService = authoritiesService;
		this.taskService = taskService;
	}

	////////////////////////////////////////////////////////////////////////////////
	// Methods

	@Transactional(readOnly = true)
	public Optional<Employee> findEmployeeById(int id) throws DataAccessException {
		return this.employeeRepository.findById(id);
	}

	@Transactional(readOnly = true)
	public Optional<Employee> findEmployeeByUsername(String username) throws DataAccessException {
		return this.employeeRepository.findByUsername(username);
	}

	@Transactional(readOnly = true)
	public Iterable<Employee> findNotAssignedToABuilding() throws DataAccessException {
		return this.employeeRepository.findNotAssignedToABuilding();
	}

	@Transactional(readOnly = true)
	public Iterable<Employee> findNotAssignedToTask(int taskId) throws DataAccessException {
		return this.employeeRepository.findNotAssignedToTask(taskId);
	}

	@Transactional(readOnly = true)
	public Iterable<Employee> findNotAssignedToTaskInBuilding(int taskId, int buildingId) throws DataAccessException {
		return this.employeeRepository.findNotAssignedToTaskInBuilding(taskId, buildingId);
	}

	@Transactional(readOnly = true)
	public Iterable<Employee> findAssignableToTask(int taskId) throws DataAccessException {
		Optional<Task> task = this.taskService.findTaskById(taskId);
		if (!task.isPresent()) {
			return new ArrayList<>();
		}
		if (task.get().getComplete()) {
			return new ArrayList<>();
		}
		if (task.get().getEmployees().size() == 0) {
			Iterable<Employee> notAssignedToTask = this.findNotAssignedToTask(taskId);
			return notAssignedToTask;
		} else {
			int buildingId = task.get().getEmployees().get(0).getBuilding().getId();
			Iterable<Employee> notAssignedToTaskInBuilding = this.findNotAssignedToTaskInBuilding(taskId, buildingId);
			return notAssignedToTaskInBuilding;
		}
	}

	@Transactional(readOnly = true)
	public Optional<Employee> findEmployeePrincipal() {
		Optional<User> user = this.userService.findPrincipal();
		if (!user.isPresent()) {
			return Optional.empty();
		} else {
			return this.employeeRepository.findByUsername(user.get().getUsername());
		}
	}

	@Transactional(readOnly = true)
	public Iterable<Employee> findAll() throws DataAccessException {
		return this.employeeRepository.findAll();
	}

	@Transactional
	public void saveEmployee(Employee employee) throws DataAccessException {
		this.employeeRepository.save(employee);
		userService.saveUser(employee.getUser());
		authoritiesService.saveAuthorities(employee.getUser().getUsername(), "employee");
	}

}
