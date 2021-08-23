package org.springframework.samples.petclinic.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Employee;
import org.springframework.samples.petclinic.model.Task;
import org.springframework.samples.petclinic.model.WorkLog;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
class WorkLogServiceTests {

	////////////////////////////////////////////////////////////////////////////////
	// Services

	@Autowired
	protected WorkLogService workLogService;

	@Autowired
	protected EmployeeService employeeService;

	@Autowired
	protected TaskService taskService;

	////////////////////////////////////////////////////////////////////////////////
	// Tests

	@Test
	void shouldFindAllWorkLogs() {
		Employee employee1 = this.employeeService.findEmployeeById(1).get();
		Task task1 = this.taskService.findTaskById(1).get();
		Collection<WorkLog> workLogs;
		// Ahora mismo hay 0 work logs
		workLogs = this.workLogService.findAll();
		assertThat(workLogs.size()).isEqualTo(0);
		// Creamos un nuevo work log
		WorkLog workLog = new WorkLog();
		workLog.setEmployee(employee1);
		workLog.setTask(task1);
		workLog.setHours(4);
		workLog.setDate(new Date());
		this.workLogService.saveWorkLog(workLog);
		// Ahora mismo debería haber 1 work log
		workLogs = this.workLogService.findAll();
		assertThat(workLogs.size()).isEqualTo(1);
	}

	@Test
	void shouldFindAllWorkLogsByEmployee() {
		Employee employee1 = this.employeeService.findEmployeeById(1).get();
		Task task1 = this.taskService.findTaskById(1).get();
		Collection<WorkLog> workLogs;
		// Ahora mismo hay 0 work logs de employee1
		workLogs = this.workLogService.findByEmployee(employee1.getId());
		assertThat(workLogs.size()).isEqualTo(0);
		// Creamos un nuevo work log con employee1
		WorkLog workLog = new WorkLog();
		workLog.setEmployee(employee1);
		workLog.setTask(task1);
		workLog.setHours(4);
		workLog.setDate(new Date());
		this.workLogService.saveWorkLog(workLog);
		// Ahora mismo debería haber 1 work log con employee1
		workLogs = this.workLogService.findByEmployee(employee1.getId());
		assertThat(workLogs.size()).isEqualTo(1);
	}

	@Test
	void shouldFindHoursLoggedByEmployeeAtDate() {
		Employee employee1 = this.employeeService.findEmployeeById(1).get();
		Task task1 = this.taskService.findTaskById(1).get();
		Integer totalHours;
		// Ahora mismo hay 0 horas registradas de employee1 en el día de hoy
		totalHours = this.workLogService.findHoursLoggedByEmployeeAtDate(employee1.getId(), new Date());
		assertThat(totalHours).isEqualTo(0);
		// Creamos un nuevo work log con employee1 y 4 horas en el día de hoy
		WorkLog workLog1 = new WorkLog();
		workLog1.setEmployee(employee1);
		workLog1.setTask(task1);
		workLog1.setHours(4);
		workLog1.setDate(new Date());
		this.workLogService.saveWorkLog(workLog1);
		// Ahora mismo debería haber 4 horas con employee1
		totalHours = this.workLogService.findHoursLoggedByEmployeeAtDate(employee1.getId(), new Date());
		assertThat(totalHours).isEqualTo(4);
		// Creamos un nuevo work log con employee1 y 3 horas en el día de hoy
		WorkLog workLog2 = new WorkLog();
		workLog2.setEmployee(employee1);
		workLog2.setTask(task1);
		workLog2.setHours(3);
		workLog2.setDate(new Date());
		this.workLogService.saveWorkLog(workLog2);
		// Ahora mismo debería haber 7 horas con employee1
		totalHours = this.workLogService.findHoursLoggedByEmployeeAtDate(employee1.getId(), new Date());
		assertThat(totalHours).isEqualTo(7);
		// Creamos un nuevo work log con employee1 y 3 horas en el día de ayer
		Calendar yesterday = Calendar.getInstance();
		yesterday.add(Calendar.DATE, -1);
		WorkLog workLog3 = new WorkLog();
		workLog3.setEmployee(employee1);
		workLog3.setTask(task1);
		workLog3.setHours(3);
		workLog3.setDate(yesterday.getTime());
		this.workLogService.saveWorkLog(workLog3);
		// Ahora mismo debería haber 7 horas con employee1
		// (las horas de ayer no cuentan)
		totalHours = this.workLogService.findHoursLoggedByEmployeeAtDate(employee1.getId(), new Date());
		assertThat(totalHours).isEqualTo(7);
		// Creamos un nuevo work log con employee1 y 3 horas en el día de mañana
		Calendar tomorrow = Calendar.getInstance();
		tomorrow.add(Calendar.DATE, 1);
		WorkLog workLog4 = new WorkLog();
		workLog4.setEmployee(employee1);
		workLog4.setTask(task1);
		workLog4.setHours(3);
		workLog4.setDate(tomorrow.getTime());
		this.workLogService.saveWorkLog(workLog4);
		// Ahora mismo debería haber 7 horas con employee1
		// (las horas de mañana no cuentan)
		totalHours = this.workLogService.findHoursLoggedByEmployeeAtDate(employee1.getId(), new Date());
		assertThat(totalHours).isEqualTo(7);
	}

	@Test
	void shouldFindHoursLoggedByEmployeeToday() {
		Employee employee1 = this.employeeService.findEmployeeById(1).get();
		Task task1 = this.taskService.findTaskById(1).get();
		Integer totalHours;
		// Ahora mismo hay 0 horas registradas de employee1 en el día de hoy
		totalHours = this.workLogService.findHoursLoggedByEmployeeToday(employee1.getId());
		assertThat(totalHours).isEqualTo(0);
		// Creamos un nuevo work log con employee1 y 4 horas en el día de hoy
		WorkLog workLog1 = new WorkLog();
		workLog1.setEmployee(employee1);
		workLog1.setTask(task1);
		workLog1.setHours(4);
		workLog1.setDate(new Date());
		this.workLogService.saveWorkLog(workLog1);
		// Ahora mismo debería haber 4 horas con employee1
		totalHours = this.workLogService.findHoursLoggedByEmployeeToday(employee1.getId());
		assertThat(totalHours).isEqualTo(4);
		// Creamos un nuevo work log con employee1 y 3 horas en el día de hoy
		WorkLog workLog2 = new WorkLog();
		workLog2.setEmployee(employee1);
		workLog2.setTask(task1);
		workLog2.setHours(3);
		workLog2.setDate(new Date());
		this.workLogService.saveWorkLog(workLog2);
		// Ahora mismo debería haber 7 horas con employee1
		totalHours = this.workLogService.findHoursLoggedByEmployeeToday(employee1.getId());
		assertThat(totalHours).isEqualTo(7);
		// Creamos un nuevo work log con employee1 y 3 horas en el día de ayer
		Calendar yesterday = Calendar.getInstance();
		yesterday.add(Calendar.DATE, -1);
		WorkLog workLog3 = new WorkLog();
		workLog3.setEmployee(employee1);
		workLog3.setTask(task1);
		workLog3.setHours(3);
		workLog3.setDate(yesterday.getTime());
		this.workLogService.saveWorkLog(workLog3);
		// Ahora mismo debería haber 7 horas con employee1
		// (las horas de ayer no cuentan)
		totalHours = this.workLogService.findHoursLoggedByEmployeeToday(employee1.getId());
		assertThat(totalHours).isEqualTo(7);
		// Creamos un nuevo work log con employee1 y 3 horas en el día de mañana
		Calendar tomorrow = Calendar.getInstance();
		tomorrow.add(Calendar.DATE, 1);
		WorkLog workLog4 = new WorkLog();
		workLog4.setEmployee(employee1);
		workLog4.setTask(task1);
		workLog4.setHours(3);
		workLog4.setDate(tomorrow.getTime());
		this.workLogService.saveWorkLog(workLog4);
		// Ahora mismo debería haber 7 horas con employee1
		// (las horas de mañana no cuentan)
		totalHours = this.workLogService.findHoursLoggedByEmployeeToday(employee1.getId());
		assertThat(totalHours).isEqualTo(7);
	}

	@Test
	void shouldFindWorkLogWithCorrectId() {
		Employee employee1 = this.employeeService.findEmployeeById(1).get();
		Task task1 = this.taskService.findTaskById(1).get();
		Collection<WorkLog> workLogs;
		// Ahora mismo hay 0 work logs
		workLogs = this.workLogService.findAll();
		assertThat(workLogs.size()).isEqualTo(0);
		// Creamos un nuevo work log
		WorkLog workLog = new WorkLog();
		workLog.setEmployee(employee1);
		workLog.setTask(task1);
		workLog.setHours(4);
		workLog.setDate(new Date());
		this.workLogService.saveWorkLog(workLog);
		// Obtenemos el work log creado por su id
		workLog = this.workLogService.findWorkLogById(workLog.getId()).get();
		// Comprobamos que es el mismo workLog
		assertThat(workLog).isNotNull();
		assertThat(workLog.getHours()).isEqualTo(4);
	}

	@Test
	@Transactional
	public void shouldInsertWorkLogIntoDatabaseAndGenerateId() {
		Employee employee1 = this.employeeService.findEmployeeById(1).get();
		Task task1 = this.taskService.findTaskById(1).get();
		Collection<WorkLog> workLogs;
		// Ahora mismo hay 0 work logs
		workLogs = this.workLogService.findAll();
		assertThat(workLogs.size()).isEqualTo(0);
		// Creamos un nuevo work log
		WorkLog workLog = new WorkLog();
		workLog.setEmployee(employee1);
		workLog.setTask(task1);
		workLog.setHours(4);
		workLog.setDate(new Date());
		this.workLogService.saveWorkLog(workLog);
		// Comprobamos que la id ha sido creada
		assertThat(workLog.getId()).isNotNull();
	}

}
