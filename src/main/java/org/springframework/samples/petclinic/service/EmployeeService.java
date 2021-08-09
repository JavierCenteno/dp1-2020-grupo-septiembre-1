package org.springframework.samples.petclinic.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Employee;
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

	@Autowired
	private UserService userService;

	@Autowired
	private AuthoritiesService authoritiesService;

	////////////////////////////////////////////////////////////////////////////////
	// Constructor

	@Autowired
	public EmployeeService(EmployeeRepository employeeRepository) {
		this.employeeRepository = employeeRepository;
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
	public Iterable<Employee> findNotAssignedToTask(int taskId) throws DataAccessException {
		return this.employeeRepository.findNotAssignedToTask(taskId);
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
