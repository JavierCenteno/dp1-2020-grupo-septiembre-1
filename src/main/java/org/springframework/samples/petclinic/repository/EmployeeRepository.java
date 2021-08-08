package org.springframework.samples.petclinic.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Employee;

public interface EmployeeRepository extends CrudRepository<Employee, Integer> {

	@Query("select e from Employee e where e.user.username = ?1")
	Optional<Employee> findByUsername(String username);

}
