package org.springframework.samples.petclinic.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Tool;

public interface ToolRepository extends CrudRepository<Tool, Integer> {

	@Query("select t from Tool t where t.building.id = ?1")
	Collection<Tool> findByBuildingId(int id);

	@Query("select t from Tool t where (t.task is null) and (t.building.id = ?1)")
	Collection<Tool> findNotAssignedToTaskInBuilding(int buildingId);

}
