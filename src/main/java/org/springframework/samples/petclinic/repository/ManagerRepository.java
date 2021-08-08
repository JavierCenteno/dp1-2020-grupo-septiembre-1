package org.springframework.samples.petclinic.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Manager;

public interface ManagerRepository extends CrudRepository<Manager, Integer> {

	@Query("select m from Manager m where m.user.username = ?1")
	Optional<Manager> findByUsername(String username);

}
