package org.springframework.samples.petclinic.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.WorkLog;
import org.springframework.samples.petclinic.repository.WorkLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WorkLogService {

	////////////////////////////////////////////////////////////////////////////////
	// Repository

	private WorkLogRepository workLogRepository;

	////////////////////////////////////////////////////////////////////////////////
	// Services

	private EmployeeService employeeService;
	private TaskService taskService;

	////////////////////////////////////////////////////////////////////////////////
	// Constructor

	@Autowired
	public WorkLogService(WorkLogRepository workLogRepository, EmployeeService employeeService,
			TaskService taskService) {
		this.workLogRepository = workLogRepository;
		this.employeeService = employeeService;
		this.taskService = taskService;
	}

	////////////////////////////////////////////////////////////////////////////////
	// Methods

	@Transactional(readOnly = true)
	public Optional<WorkLog> findWorkLogById(int id) throws DataAccessException {
		return this.workLogRepository.findById(id);
	}

	@Transactional(readOnly = true)
	public Collection<WorkLog> findByEmployeeAndTask(int employeeId, int taskId) throws DataAccessException {
		return this.workLogRepository.findByEmployeeAndTask(employeeId, taskId);
	}

	@Transactional(readOnly = true)
	public Collection<WorkLog> findAll() {
		Collection<WorkLog> allWorkLogs = new ArrayList<WorkLog>();
		for (WorkLog workLog : this.workLogRepository.findAll()) {
			allWorkLogs.add(workLog);
		}
		return allWorkLogs;
	}

	@Transactional(readOnly = true)
	public Integer findHoursLoggedIntoTaskByEmployeeAtDate(int taskId, int employeeId, Date date)
			throws DataAccessException {
		if (date == null) {
			return null;
		}
		if (!this.employeeService.findEmployeeById(employeeId).isPresent()) {
			return null;
		}
		if (!this.taskService.findTaskById(taskId).isPresent()) {
			return null;
		}
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		Integer totalHours = 0;
		Iterable<WorkLog> allWorkLogsOfTask = this.findByEmployeeAndTask(employeeId, taskId);
		for (WorkLog workLog : allWorkLogsOfTask) {
			if (formatter.format(workLog.getDate()).equals(formatter.format(date))) {
				totalHours += workLog.getHours();
			}
		}
		return totalHours;
	}

	@Transactional
	public void saveWorkLog(WorkLog workLog) throws DataAccessException {
		this.workLogRepository.save(workLog);
	}

}
