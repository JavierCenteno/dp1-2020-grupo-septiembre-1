package org.springframework.samples.petclinic.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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
	public Optional<Building> findBuildingById(int id) {
		return this.buildingRepository.findById(id);
	}

	@Transactional(readOnly = true)
	public Collection<Building> findAll() {
		Collection<Building> allBuildings = new ArrayList<Building>();
		for(Building building : this.buildingRepository.findAll()) {
			allBuildings.add(building);
		}
		return allBuildings;
	}

	@Transactional
	public void saveBuilding(Building building) {
		this.buildingRepository.save(building);
	}

}
