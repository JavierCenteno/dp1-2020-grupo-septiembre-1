package org.springframework.samples.petclinic.repository;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Employee;

public interface EmployeeRepository extends CrudRepository<Employee, Integer> {

	@Query("select e from Employee e where e.user.username = ?1")
	Optional<Employee> findByUsername(String username);

	@Query("select e from Employee e where e.building is null")
	Collection<Employee> findNotAssignedToABuilding();

	@Query("select e from Employee e where not exists (select distinct ee from Employee ee inner join ee.tasks t where t.id = ?1 and ee.id = e.id)")
	Collection<Employee> findNotAssignedToTask(int taskId);

	@Query("select e from Employee e where not exists (select distinct ee from Employee ee inner join ee.tasks t where t.id = ?1 and ee.id = e.id) and e.building.id = ?2")
	Collection<Employee> findNotAssignedToTaskInBuilding(int taskId, int buildingId);

}
