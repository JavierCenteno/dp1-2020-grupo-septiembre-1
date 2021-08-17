package org.springframework.samples.petclinic.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.WorkLog;

public interface WorkLogRepository extends CrudRepository<WorkLog, Integer> {

	@Query("select w from WorkLog w where w.employee.id = ?1")
	Collection<WorkLog> findByEmployee(int employeeId);

}
