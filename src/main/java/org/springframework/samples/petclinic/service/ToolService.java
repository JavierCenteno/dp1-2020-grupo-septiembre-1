package org.springframework.samples.petclinic.service;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Task;
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

	private TaskService taskService;

	////////////////////////////////////////////////////////////////////////////////
	// Constructor

	@Autowired
	public ToolService(ToolRepository toolRepository, TaskService taskService) {
		this.toolRepository = toolRepository;
		this.taskService = taskService;
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
	public Iterable<Tool> findNotAssignedToTaskInBuilding(int taskId, int buildingId) throws DataAccessException {
		return this.toolRepository.findNotAssignedToTaskInBuilding(taskId, buildingId);
	}

	@Transactional(readOnly = true)
	public Iterable<Tool> findAssignableToTask(int taskId) throws DataAccessException {
		Optional<Task> task = this.taskService.findTaskById(taskId);
		if (!task.isPresent()) {
			return new ArrayList<>();
		}
		if (task.get().getComplete()) {
			return new ArrayList<>();
		}
		if (task.get().getEmployees().size() == 0) {
			return new ArrayList<>();
		}
		int buildingId = task.get().getEmployees().get(0).getBuilding().getId();
		Iterable<Tool> notAssignedToTaskInBuilding = this.findNotAssignedToTaskInBuilding(taskId, buildingId);
		return notAssignedToTaskInBuilding;
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
