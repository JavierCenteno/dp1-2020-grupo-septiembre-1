package org.springframework.samples.petclinic.service;

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

	@Transactional
	public void saveTask(Task task) throws DataAccessException {
		this.taskRepository.save(task);
	}

}
