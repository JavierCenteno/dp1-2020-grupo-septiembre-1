package org.springframework.samples.petclinic.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.WorkLog;

public interface WorkLogRepository extends CrudRepository<WorkLog, Integer> {
}
