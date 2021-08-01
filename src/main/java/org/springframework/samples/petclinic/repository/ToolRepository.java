package org.springframework.samples.petclinic.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Tool;

public interface ToolRepository extends CrudRepository<Tool, Integer> {
}
