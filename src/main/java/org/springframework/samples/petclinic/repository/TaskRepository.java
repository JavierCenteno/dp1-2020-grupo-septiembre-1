package org.springframework.samples.petclinic.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Task;

public interface TaskRepository extends CrudRepository<Task, Integer> {

	@Query("select t from Task t where t.employees is empty")
	Iterable<Task> findUnassigned();

}
