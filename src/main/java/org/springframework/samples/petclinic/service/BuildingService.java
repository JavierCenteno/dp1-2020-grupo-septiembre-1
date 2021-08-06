package org.springframework.samples.petclinic.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Building;
import org.springframework.samples.petclinic.repository.BuildingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BuildingService {

	////////////////////////////////////////////////////////////////////////////////
	// Repository

	private BuildingRepository buildingRepository;

	////////////////////////////////////////////////////////////////////////////////
	// Services

	////////////////////////////////////////////////////////////////////////////////
	// Constructor

	@Autowired
	public BuildingService(BuildingRepository buildingRepository) {
		this.buildingRepository = buildingRepository;
	}

	////////////////////////////////////////////////////////////////////////////////
	// Methods

	@Transactional(readOnly = true)
	public Optional<Building> findBuildingById(int id) throws DataAccessException {
		return this.buildingRepository.findById(id);
	}

	@Transactional(readOnly = true)
	public Iterable<Building> findAll() throws DataAccessException {
		return this.buildingRepository.findAll();
	}

	@Transactional
	public void saveBuilding(Building building) throws DataAccessException {
		this.buildingRepository.save(building);
	}

}
