package org.springframework.samples.petclinic.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Manager;
import org.springframework.samples.petclinic.model.User;
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

	private UserService userService;

	private AuthoritiesService authoritiesService;

	////////////////////////////////////////////////////////////////////////////////
	// Constructor

	@Autowired
	public ManagerService(ManagerRepository managerRepository, UserService userService,
			AuthoritiesService authoritiesService) {
		this.managerRepository = managerRepository;
		this.userService = userService;
		this.authoritiesService = authoritiesService;
	}

	////////////////////////////////////////////////////////////////////////////////
	// Methods

	@Transactional(readOnly = true)
	public Optional<Manager> findManagerById(int id) {
		return this.managerRepository.findById(id);
	}

	@Transactional(readOnly = true)
	public Optional<Manager> findManagerByUsername(String username) {
		return this.managerRepository.findByUsername(username);
	}

	@Transactional(readOnly = true)
	public Optional<Manager> findManagerPrincipal() {
		Optional<User> user = this.userService.findPrincipal();
		if (!user.isPresent()) {
			return Optional.empty();
		} else {
			return this.managerRepository.findByUsername(user.get().getUsername());
		}
	}

	@Transactional(readOnly = true)
	public Collection<Manager> findAll() {
		Collection<Manager> allManagers = new ArrayList<Manager>();
		for (Manager manager : this.managerRepository.findAll()) {
			allManagers.add(manager);
		}
		return allManagers;
	}

	@Transactional
	public void saveManager(Manager manager) {
		this.managerRepository.save(manager);
		userService.saveUser(manager.getUser());
		authoritiesService.saveAuthorities(manager.getUser().getUsername(), "manager");
	}

}
