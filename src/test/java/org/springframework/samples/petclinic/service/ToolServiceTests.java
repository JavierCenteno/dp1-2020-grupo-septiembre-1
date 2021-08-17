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
import org.springframework.samples.petclinic.model.Tool;
import org.springframework.samples.petclinic.util.EntityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
class ToolServiceTests {

	////////////////////////////////////////////////////////////////////////////////
	// Services

	@Autowired
	protected ToolService toolService;

	@Autowired
	protected TaskService taskService;

	@Autowired
	protected EmployeeService employeeService;

	////////////////////////////////////////////////////////////////////////////////
	// Tests

	@Test
	void shouldFindAllTools() {
		Collection<Tool> tools = this.toolService.findAll();
		assertThat(tools.size()).isEqualTo(4);
		Tool tool1 = EntityUtils.getById(tools, Tool.class, 1);
		assertThat(tool1.getName()).isEqualTo("tool1");
		Tool tool2 = EntityUtils.getById(tools, Tool.class, 2);
		assertThat(tool2.getName()).isEqualTo("tool2");
		Tool tool3 = EntityUtils.getById(tools, Tool.class, 3);
		assertThat(tool3.getName()).isEqualTo("tool3");
		Tool tool4 = EntityUtils.getById(tools, Tool.class, 4);
		assertThat(tool4.getName()).isEqualTo("tool4");
	}

	@Test
	void shouldFindAllToolsAssignableToTask() {
		Tool tool1 = this.toolService.findToolById(1).get();
		Task task1 = this.taskService.findTaskById(1).get();
		Employee employee1 = this.employeeService.findEmployeeById(1).get();
		Collection<Tool> tools;
		// task1 no tiene edificio asignado aún
		// así que debe tener 0 herramientas asignables
		tools = this.toolService.findAssignableToTask(1);
		assertThat(tools.size()).isEqualTo(0);
		// task1 tiene ahora el edificio de employee1 (building1)
		// así que debe tener asignables todas las herramientas de building1
		task1.addEmployee(employee1);
		this.taskService.saveTask(task1);
		tools = this.toolService.findAssignableToTask(1);
		assertThat(tools.size()).isEqualTo(2);
		// a task1 se le ha asignado tool1
		// así que tool1 ya no es asignable a task1, el total de herramientas es 1 menos
		tool1.setTask(task1);
		this.toolService.saveTool(tool1);
		tools = this.toolService.findAssignableToTask(1);
		assertThat(tools.size()).isEqualTo(1);
	}

	@Test
	void shouldFindToolWithCorrectId() {
		Optional<Tool> tool = this.toolService.findToolById(1);
		assertThat(tool.get()).isNotNull();
		assertThat(tool.get().getName()).isEqualTo("tool1");
	}

	@Test
	@Transactional
	public void shouldInsertToolIntoDatabaseAndGenerateId() {
		Tool tool = new Tool();
		tool.setName("tool5");
		this.toolService.saveTool(tool);
		assertThat(tool.getId()).isNotNull();
	}

}
