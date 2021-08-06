package org.springframework.samples.petclinic.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Manager;
import org.springframework.samples.petclinic.repository.ManagerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ManagerService {

	////////////////////////////////////////////////////////////////////////////////
	// Repository

	private ManagerRepository managerRepository;

	////////////////////////////////////////////////////////////////////////////////
	// Services

	@Autowired
	private UserService userService;

	@Autowired
	private AuthoritiesService authoritiesService;

	////////////////////////////////////////////////////////////////////////////////
	// Constructor

	@Autowired
	public ManagerService(ManagerRepository managerRepository) {
		this.managerRepository = managerRepository;
	}

	////////////////////////////////////////////////////////////////////////////////
	// Methods

	@Transactional(readOnly = true)
	public Optional<Manager> findManagerById(int id) {
		return this.managerRepository.findById(id);
	}

	@Transactional(readOnly = true)
	public Iterable<Manager> findAll() {
		return this.managerRepository.findAll();
	}

	@Transactional
	public void saveManager(Manager manager) {
		this.managerRepository.save(manager);
		userService.saveUser(manager.getUser());
		authoritiesService.saveAuthorities(manager.getUser().getUsername(), "manager");
	}

}
