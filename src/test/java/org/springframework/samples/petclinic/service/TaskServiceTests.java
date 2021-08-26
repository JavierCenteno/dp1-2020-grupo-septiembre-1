package org.springframework.samples.petclinic.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Employee;
import org.springframework.samples.petclinic.model.Task;
import org.springframework.samples.petclinic.util.EntityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
class TaskServiceTests {

	////////////////////////////////////////////////////////////////////////////////
	// Services

	@Autowired
	protected TaskService taskService;

	@Autowired
	protected ToolService toolService;

	@Autowired
	protected EmployeeService employeeService;

	////////////////////////////////////////////////////////////////////////////////
	// Tests

	@Test
	void shouldFindAllTasks() {
		Collection<Task> tasks = this.taskService.findAll();
		assertThat(tasks.size()).isEqualTo(4);
		Task task1 = EntityUtils.getById(tasks, Task.class, 1);
		assertThat(task1.getName()).isEqualTo("task1");
		Task task2 = EntityUtils.getById(tasks, Task.class, 2);
		assertThat(task2.getName()).isEqualTo("task2");
		Task task3 = EntityUtils.getById(tasks, Task.class, 3);
		assertThat(task3.getName()).isEqualTo("task3");
		Task task4 = EntityUtils.getById(tasks, Task.class, 4);
		assertThat(task4.getName()).isEqualTo("task4");
	}

	@Test
	void shouldFindAllUnassignedTasks() {
		Employee employee1 = this.employeeService.findEmployeeById(1).get();
		Task task1 = this.taskService.findTaskById(1).get();
		task1.addEmployee(employee1);
		this.taskService.saveTask(task1);
		Collection<Task> tasks = this.taskService.findUnassigned();
		// Normalmente hay 4 tareas sin asignar
		// Ahora se ha asignado 1 con lo que hay 4 - 1 = 3
		assertThat(tasks.size()).isEqualTo(3);
	}

	@Test
	void shouldFindAllAssignedAndUncompleteTasks() {
		Employee employee1 = this.employeeService.findEmployeeById(1).get();
		Task task1 = this.taskService.findTaskById(1).get();
		Collection<Task> tasks;
		// task1 no tiene empleado asignada aún
		// así que debe haber 0 tareas con empleados y sin completar
		tasks = this.taskService.findAssignedAndUncomplete();
		assertThat(tasks.size()).isEqualTo(0);
		// task1 tiene ahora employee1 asignada pero no ha sido completada
		// así que debe haber una tarea más sin empleados y sin completar
		task1.addEmployee(employee1);
		this.taskService.saveTask(task1);
		tasks = this.taskService.findAssignedAndUncomplete();
		assertThat(tasks.size()).isEqualTo(1);
		// task1 tiene ahora employee1 asignada y ha sido completada
		// así que debe haber una tarea menos sin empleados y sin completar
		task1.setComplete(true);
		this.taskService.saveTask(task1);
		tasks = this.taskService.findAssignedAndUncomplete();
		assertThat(tasks.size()).isEqualTo(0);
	}

	@Test
	void shouldFindAllUncompleteTasks() {
		Task task1 = this.taskService.findTaskById(1).get();
		Collection<Task> tasks;
		// task1 no ha sido completada aún
		// así que debe haber 4 tareas sin completar
		tasks = this.taskService.findUncomplete();
		assertThat(tasks.size()).isEqualTo(4);
		// task1 ha sido completada ahora
		// así que debe haber 3 tareas sin completar
		task1.setComplete(true);
		this.taskService.saveTask(task1);
		tasks = this.taskService.findUncomplete();
		assertThat(tasks.size()).isEqualTo(3);
	}

	@Test
	void shouldFindAllAssignedToEmployeeAndNotCompleteTasks() {
		Employee employee1 = this.employeeService.findEmployeeById(1).get();
		Task task1 = this.taskService.findTaskById(1).get();
		Collection<Task> tasks;
		// task1 no tiene empleado asignada aún
		// así que debe haber 0 tareas asignadas a employee1 y sin completar
		tasks = this.taskService.findAssignedToEmployeeAndNotComplete(employee1.getId());
		assertThat(tasks.size()).isEqualTo(0);
		// task1 tiene ahora employee1 asignada pero no ha sido completada
		// así que debe haber una tarea más asignada a employee1 y sin completar
		task1.addEmployee(employee1);
		this.taskService.saveTask(task1);
		tasks = this.taskService.findAssignedToEmployeeAndNotComplete(employee1.getId());
		assertThat(tasks.size()).isEqualTo(1);
		// task1 tiene ahora employee1 asignada y ha sido completada
		// así que debe haber una tarea menos asignada a employee1 y sin completar
		task1.setComplete(true);
		this.taskService.saveTask(task1);
		tasks = this.taskService.findAssignedToEmployeeAndNotComplete(employee1.getId());
		assertThat(tasks.size()).isEqualTo(0);
	}

	@Test
	void shouldFindTaskWithCorrectId() {
		Optional<Task> task = this.taskService.findTaskById(1);
		assertThat(task.get()).isNotNull();
		assertThat(task.get().getName()).isEqualTo("task1");
	}

	@Test
	@Transactional
	public void shouldInsertTaskIntoDatabaseAndGenerateId() {
		Task task = new Task();
		task.setName("task5");
		task.setIncome(0);
		this.taskService.saveTask(task);
		assertThat(task.getId()).isNotNull();
	}

}
