package org.springframework.samples.petclinic.service;

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

	////////////////////////////////////////////////////////////////////////////////
	// Constructor

	@Autowired
	public WorkLogService(WorkLogRepository workLogRepository) {
		this.workLogRepository = workLogRepository;
	}

	////////////////////////////////////////////////////////////////////////////////
	// Methods

	@Transactional(readOnly = true)
	public Optional<WorkLog> findWorkLogById(int id) throws DataAccessException {
		return this.workLogRepository.findById(id);
	}

	@Transactional
	public void saveWorkLog(WorkLog workLog) throws DataAccessException {
		this.workLogRepository.save(workLog);
	}

}
