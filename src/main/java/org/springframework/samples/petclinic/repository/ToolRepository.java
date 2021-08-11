package org.springframework.samples.petclinic.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Tool;

public interface ToolRepository extends CrudRepository<Tool, Integer> {

	@Query("select t from Tool t where t.building.id = ?1")
	Iterable<Tool> findByBuildingId(int id);

	@Query("select t from Tool t where t.task.id != ?1 and t.building.id = ?2")
	Iterable<Tool> findNotAssignedToTaskInBuilding(int taskId, int buildingId);

}
