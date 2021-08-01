package org.springframework.samples.petclinic.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Building;

public interface BuildingRepository extends CrudRepository<Building, Integer> {
}
