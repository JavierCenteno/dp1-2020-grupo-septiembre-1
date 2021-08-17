package org.springframework.samples.petclinic.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Task;

public interface TaskRepository extends CrudRepository<Task, Integer> {

	@Query("select t from Task t where t.employees is empty")
	Collection<Task> findUnassigned();

	@Query("select t from Task t where t.employees is not empty and t.complete = false")
	Collection<Task> findAssignedAndUncomplete();

	@Query("select t from Task t where t.complete = false")
	Collection<Task> findUncomplete();

	@Query("select distinct t from Task t inner join t.employees e where e.id = ?1 and t.complete = false")
	Collection<Task> findAssignedToEmployeeAndNotComplete(int employeeId);

}
