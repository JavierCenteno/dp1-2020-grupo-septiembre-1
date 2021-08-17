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
import org.springframework.samples.petclinic.model.Building;
import org.springframework.samples.petclinic.model.Employee;
import org.springframework.samples.petclinic.model.Task;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.util.EntityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
class EmployeeServiceTests {

	////////////////////////////////////////////////////////////////////////////////
	// Services

	@Autowired
	protected EmployeeService employeeService;

	@Autowired
	protected UserService userService;

	@Autowired
	protected AuthoritiesService authoritiesService;

	@Autowired
	protected BuildingService buildingService;

	@Autowired
	protected TaskService taskService;

	////////////////////////////////////////////////////////////////////////////////
	// Tests

	@Test
	void shouldFindAllEmployees() {
		Collection<Employee> employees = this.employeeService.findAll();
		assertThat(employees.size()).isEqualTo(8);
		Employee employee1 = EntityUtils.getById(employees, Employee.class, 1);
		assertThat(employee1.getName()).isEqualTo("Alice Employee");
		assertThat(employee1.getEmail()).isEqualTo("alice@employee.com");
		assertThat(employee1.getAddress()).isEqualTo("c/Employee 1");
		Employee employee2 = EntityUtils.getById(employees, Employee.class, 2);
		assertThat(employee2.getName()).isEqualTo("Bob Employee");
		assertThat(employee2.getEmail()).isEqualTo("bob@employee.com");
		assertThat(employee2.getAddress()).isEqualTo("c/Employee 2");
		Employee employee3 = EntityUtils.getById(employees, Employee.class, 3);
		assertThat(employee3.getName()).isEqualTo("Charles Employee");
		assertThat(employee3.getEmail()).isEqualTo("charles@employee.com");
		assertThat(employee3.getAddress()).isEqualTo("c/Employee 3");
		Employee employee4 = EntityUtils.getById(employees, Employee.class, 4);
		assertThat(employee4.getName()).isEqualTo("Diane Employee");
		assertThat(employee4.getEmail()).isEqualTo("diane@employee.com");
		assertThat(employee4.getAddress()).isEqualTo("c/Employee 4");
		Employee employee5 = EntityUtils.getById(employees, Employee.class, 5);
		assertThat(employee5.getName()).isEqualTo("Ernest Employee");
		assertThat(employee5.getEmail()).isEqualTo("ernest@employee.com");
		assertThat(employee5.getAddress()).isEqualTo("c/Employee 5");
		Employee employee6 = EntityUtils.getById(employees, Employee.class, 6);
		assertThat(employee6.getName()).isEqualTo("Fatima Employee");
		assertThat(employee6.getEmail()).isEqualTo("fatima@employee.com");
		assertThat(employee6.getAddress()).isEqualTo("c/Employee 6");
		Employee employee7 = EntityUtils.getById(employees, Employee.class, 7);
		assertThat(employee7.getName()).isEqualTo("Gloria Employee");
		assertThat(employee7.getEmail()).isEqualTo("gloria@employee.com");
		assertThat(employee7.getAddress()).isEqualTo("c/Employee 7");
		Employee employee8 = EntityUtils.getById(employees, Employee.class, 8);
		assertThat(employee8.getName()).isEqualTo("Hector Employee");
		assertThat(employee8.getEmail()).isEqualTo("hector@employee.com");
		assertThat(employee8.getAddress()).isEqualTo("c/Employee 8");
	}

	@Test
	void shouldFindAllEmployeesNotAssignedToABuilding() {
		Building building1 = this.buildingService.findBuildingById(1).get();
		Collection<Employee> employees;
		// Ahora mismo hay 2 empleados no asignados a un edificio
		employees = this.employeeService.findNotAssignedToABuilding();
		assertThat(employees.size()).isEqualTo(2);
		// Asignamos a employee7 (que no está asignado) a building1
		Employee employee7 = this.employeeService.findEmployeeByUsername("employee7").get();
		employee7.setBuilding(building1);
		this.employeeService.saveEmployee(employee7);
		// Ahora mismo debería haber un empleado menos no asignado
		employees = this.employeeService.findNotAssignedToABuilding();
		assertThat(employees.size()).isEqualTo(1);
	}

	@Test
	void shouldFindAllEmployeesNotAssignedToTask() {
		Task task1 = this.taskService.findTaskById(1).get();
		Collection<Employee> employees;
		// Ahora mismo hay 8 empleados no asignados a task1
		employees = this.employeeService.findNotAssignedToTask(1);
		assertThat(employees.size()).isEqualTo(8);
		// Asignamos task1 a employee1
		Employee employee1 = this.employeeService.findEmployeeByUsername("employee1").get();
		employee1.addTask(task1);
		this.employeeService.saveEmployee(employee1);
		task1.addEmployee(employee1);
		this.taskService.saveTask(task1);
		// Ahora mismo debería haber un empleado menos no asignado a task1
		employees = this.employeeService.findNotAssignedToTask(1);
		assertThat(employees.size()).isEqualTo(7);
	}

	@Test
	void shouldFindAllEmployeesNotAssignedToTaskInBuilding() {
		Task task1 = this.taskService.findTaskById(1).get();
		Collection<Employee> employees;
		// Ahora mismo hay 4 empleados en building1 no asignados a task1
		employees = this.employeeService.findNotAssignedToTaskInBuilding(1, 1);
		assertThat(employees.size()).isEqualTo(4);
		// Asignamos task1 a employee1
		Employee employee1 = this.employeeService.findEmployeeByUsername("employee1").get();
		employee1.addTask(task1);
		this.employeeService.saveEmployee(employee1);
		task1.addEmployee(employee1);
		this.taskService.saveTask(task1);
		// Ahora mismo debería haber un empleado menos en building1 no asignado a task1
		employees = this.employeeService.findNotAssignedToTaskInBuilding(1, 1);
		assertThat(employees.size()).isEqualTo(3);
	}

	@Test
	void shouldFindAllEmployeesAssignableToTask() {
		Task task1 = this.taskService.findTaskById(1).get();
		Collection<Employee> employees;
		// Ahora mismo task1 no tiene edificio asignado,
		// por lo que cualquier empleado (8 en total) es asignable a task1
		// (menos los empleados ya asignados, que son 0, dejando el total en 8)
		employees = this.employeeService.findAssignableToTask(1);
		assertThat(employees.size()).isEqualTo(8);
		// Asignamos task1 a employee1
		Employee employee1 = this.employeeService.findEmployeeByUsername("employee1").get();
		employee1.addTask(task1);
		this.employeeService.saveEmployee(employee1);
		task1.addEmployee(employee1);
		this.taskService.saveTask(task1);
		// Como task1 está asignada a un empleado de edificio1,
		// cualquier empleado de edificio1 (4 en total) es asignable a task1
		// (menos los empleados ya asignados, que son 1, dejando el total en 3)
		employees = this.employeeService.findAssignableToTask(1);
		assertThat(employees.size()).isEqualTo(3);
	}

	@Test
	void shouldFindEmployeeWithCorrectId() {
		Optional<Employee> employee = this.employeeService.findEmployeeById(1);
		assertThat(employee.get()).isNotNull();
		assertThat(employee.get().getName()).isEqualTo("Alice Employee");
		assertThat(employee.get().getEmail()).isEqualTo("alice@employee.com");
		assertThat(employee.get().getAddress()).isEqualTo("c/Employee 1");
	}

	@Test
	void shouldFindEmployeeWithCorrectUsername() {
		Optional<Employee> employee = this.employeeService.findEmployeeByUsername("employee1");
		assertThat(employee.get()).isNotNull();
		assertThat(employee.get().getName()).isEqualTo("Alice Employee");
		assertThat(employee.get().getEmail()).isEqualTo("alice@employee.com");
		assertThat(employee.get().getAddress()).isEqualTo("c/Employee 1");
	}

	@Test
	@Transactional
	public void shouldInsertEmployeeIntoDatabaseAndGenerateId() {
		User user = new User();
		user.setUsername("employee9");
		user.setPassword("employee9");
		user.setEnabled(true);
		this.userService.saveUser(user);
		user = this.userService.findUser("employee9").get();
		Authorities authorities = new Authorities();
		authorities.setAuthority("employee");
		authorities.setUser(user);
		this.authoritiesService.saveAuthorities(authorities);
		Set<Authorities> authoritiesSet = new HashSet<>();
		authoritiesSet.add(authorities);
		user.setAuthorities(authoritiesSet);
		this.userService.saveUser(user);
		Employee employee = new Employee();
		employee.setUser(user);
		employee.setName("Ignatius Employee");
		employee.setEmail("ignatius@employee.com");
		employee.setAddress("c/Employee 9");
		this.employeeService.saveEmployee(employee);
		assertThat(employee.getId()).isNotNull();
	}

}
