package org.springframework.samples.petclinic.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Authorities;
import org.springframework.samples.petclinic.model.Manager;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.util.EntityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
class ManagerServiceTests {

	////////////////////////////////////////////////////////////////////////////////
	// Services

	@Autowired
	protected ManagerService managerService;

	@Autowired
	protected UserService userService;

	@Autowired
	protected AuthoritiesService authoritiesService;

	////////////////////////////////////////////////////////////////////////////////
	// Tests

	@Test
	void shouldFindAllManagers() {
		Collection<Manager> managers = this.managerService.findAll();
		assertThat(managers.size()).isEqualTo(4);
		Manager manager1 = EntityUtils.getById(managers, Manager.class, 1);
		assertThat(manager1.getName()).isEqualTo("Alice Manager");
		assertThat(manager1.getEmail()).isEqualTo("alice@manager.com");
		assertThat(manager1.getAddress()).isEqualTo("c/Manager 1");
		Manager manager2 = EntityUtils.getById(managers, Manager.class, 2);
		assertThat(manager2.getName()).isEqualTo("Bob Manager");
		assertThat(manager2.getEmail()).isEqualTo("bob@manager.com");
		assertThat(manager2.getAddress()).isEqualTo("c/Manager 2");
		Manager manager3 = EntityUtils.getById(managers, Manager.class, 3);
		assertThat(manager3.getName()).isEqualTo("Charles Manager");
		assertThat(manager3.getEmail()).isEqualTo("charles@manager.com");
		assertThat(manager3.getAddress()).isEqualTo("c/Manager 3");
		Manager manager4 = EntityUtils.getById(managers, Manager.class, 4);
		assertThat(manager4.getName()).isEqualTo("Diane Manager");
		assertThat(manager4.getEmail()).isEqualTo("diane@manager.com");
		assertThat(manager4.getAddress()).isEqualTo("c/Manager 4");
	}

	@Test
	void shouldFindManagerWithCorrectId() {
		Optional<Manager> manager = this.managerService.findManagerById(1);
		assertThat(manager.get()).isNotNull();
		assertThat(manager.get().getName()).isEqualTo("Alice Manager");
		assertThat(manager.get().getEmail()).isEqualTo("alice@manager.com");
		assertThat(manager.get().getAddress()).isEqualTo("c/Manager 1");
	}

	@Test
	void shouldFindManagerWithCorrectUsername() {
		Optional<Manager> manager = this.managerService.findManagerByUsername("manager1");
		assertThat(manager.get()).isNotNull();
		assertThat(manager.get().getName()).isEqualTo("Alice Manager");
		assertThat(manager.get().getEmail()).isEqualTo("alice@manager.com");
		assertThat(manager.get().getAddress()).isEqualTo("c/Manager 1");
	}

	@Test
	@Transactional
	public void shouldInsertManagerIntoDatabaseAndGenerateId() {
		User user = new User();
		user.setUsername("manager5");
		user.setPassword("manager5");
		user.setEnabled(true);
		this.userService.saveUser(user);
		user = this.userService.findUser("manager5").get();
		Authorities authorities = new Authorities();
		authorities.setAuthority("manager");
		authorities.setUser(user);
		this.authoritiesService.saveAuthorities(authorities);
		Set<Authorities> authoritiesSet = new HashSet<>();
		authoritiesSet.add(authorities);
		user.setAuthorities(authoritiesSet);
		this.userService.saveUser(user);
		Manager manager = new Manager();
		manager.setUser(user);
		manager.setName("Ernest Manager");
		manager.setEmail("ernest@manager.com");
		manager.setAddress("c/Manager 5");
		this.managerService.saveManager(manager);
		assertThat(manager.getId()).isNotNull();
	}

}
