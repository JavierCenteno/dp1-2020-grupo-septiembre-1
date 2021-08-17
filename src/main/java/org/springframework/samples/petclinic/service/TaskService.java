package org.springframework.samples.petclinic.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Task;
import org.springframework.samples.petclinic.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TaskService {

	////////////////////////////////////////////////////////////////////////////////
	// Repository

	private TaskRepository taskRepository;

	////////////////////////////////////////////////////////////////////////////////
	// Services

	////////////////////////////////////////////////////////////////////////////////
	// Constructor

	@Autowired
	public TaskService(TaskRepository taskRepository) {
		this.taskRepository = taskRepository;
	}

	////////////////////////////////////////////////////////////////////////////////
	// Methods

	@Transactional(readOnly = true)
	public Optional<Task> findTaskById(int id) throws DataAccessException {
		return this.taskRepository.findById(id);
	}

	@Transactional(readOnly = true)
	public Collection<Task> findUnassigned() throws DataAccessException {
		return this.taskRepository.findUnassigned();
	}

	@Transactional(readOnly = true)
	public Collection<Task> findAssignedAndUncomplete() throws DataAccessException {
		return this.taskRepository.findAssignedAndUncomplete();
	}

	@Transactional(readOnly = true)
	public Collection<Task> findUncomplete() throws DataAccessException {
		return this.taskRepository.findUncomplete();
	}

	@Transactional(readOnly = true)
	public Collection<Task> findAssignedToEmployeeAndNotComplete(int employeeId) throws DataAccessException {
		return this.taskRepository.findAssignedToEmployeeAndNotComplete(employeeId);
	}

	@Transactional(readOnly = true)
	public Collection<Task> findAll() {
		Collection<Task> allTasks = new ArrayList<Task>();
		for (Task task : this.taskRepository.findAll()) {
			allTasks.add(task);
		}
		return allTasks;
	}

	@Transactional
	public void saveTask(Task task) throws DataAccessException {
		this.taskRepository.save(task);
	}

}
