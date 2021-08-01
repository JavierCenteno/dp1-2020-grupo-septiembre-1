package org.springframework.samples.petclinic.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Employee;
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

	@Transactional
	public void saveEmployee(Employee employee) throws DataAccessException {
		this.employeeRepository.save(employee);
		userService.saveUser(employee.getUser());
		authoritiesService.saveAuthorities(employee.getUser().getUsername(), "employee");
	}

}
