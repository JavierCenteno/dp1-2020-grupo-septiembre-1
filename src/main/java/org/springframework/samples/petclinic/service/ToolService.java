package org.springframework.samples.petclinic.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Tool;
import org.springframework.samples.petclinic.repository.ToolRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ToolService {

	////////////////////////////////////////////////////////////////////////////////
	// Repository

	private ToolRepository toolRepository;

	////////////////////////////////////////////////////////////////////////////////
	// Services

	////////////////////////////////////////////////////////////////////////////////
	// Constructor

	@Autowired
	public ToolService(ToolRepository toolRepository) {
		this.toolRepository = toolRepository;
	}

	////////////////////////////////////////////////////////////////////////////////
	// Methods

	@Transactional(readOnly = true)
	public Optional<Tool> findToolById(int id) throws DataAccessException {
		return this.toolRepository.findById(id);
	}

	@Transactional(readOnly = true)
	public Iterable<Tool> findByBuildingId(int buildingId) throws DataAccessException {
		return this.toolRepository.findByBuildingId(buildingId);
	}

	@Transactional(readOnly = true)
	public Iterable<Tool> findNotAssignedToTask(int taskId) throws DataAccessException {
		return this.toolRepository.findNotAssignedToTask(taskId);
	}

	@Transactional(readOnly = true)
	public Iterable<Tool> findAll() throws DataAccessException {
		return this.toolRepository.findAll();
	}

	@Transactional
	public void saveTool(Tool tool) throws DataAccessException {
		this.toolRepository.save(tool);
	}

}
