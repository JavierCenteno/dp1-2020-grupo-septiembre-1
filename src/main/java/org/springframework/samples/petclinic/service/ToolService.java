package org.springframework.samples.petclinic.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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
	public Optional<Tool> findToolById(int id) {
		return this.toolRepository.findById(id);
	}

	@Transactional(readOnly = true)
	public Collection<Tool> findByBuildingId(int buildingId) {
		return this.toolRepository.findByBuildingId(buildingId);
	}

	@Transactional(readOnly = true)
	public Collection<Tool> findNotAssignedToTaskInBuilding(int buildingId) {
		return this.toolRepository.findNotAssignedToTaskInBuilding(buildingId);
	}

	@Transactional(readOnly = true)
	public Collection<Tool> findAssignableToTask(int taskId) {
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
		Collection<Tool> notAssignedToTaskInBuilding = this.findNotAssignedToTaskInBuilding(buildingId);
		return notAssignedToTaskInBuilding;
	}

	@Transactional(readOnly = true)
	public Collection<Tool> findAll() {
		Collection<Tool> allTools = new ArrayList<Tool>();
		for (Tool tool : this.toolRepository.findAll()) {
			allTools.add(tool);
		}
		return allTools;
	}

	@Transactional
	public void saveTool(Tool tool) {
		this.toolRepository.save(tool);
	}

}
