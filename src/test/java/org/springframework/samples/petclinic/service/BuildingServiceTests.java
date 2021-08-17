package org.springframework.samples.petclinic.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Building;
import org.springframework.samples.petclinic.util.EntityUtils;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
class BuildingServiceTests {

	////////////////////////////////////////////////////////////////////////////////
	// Services

	@Autowired
	protected BuildingService buildingService;	

	////////////////////////////////////////////////////////////////////////////////
	// Tests

	@Test
	void shouldFindBuildings() {
		Collection<Building> buildings = this.buildingService.findAll();
		
		Building building = EntityUtils.getById(buildings, Building.class, 1);

		assertThat(building.getName()).isEqualTo("building1");
		assertThat(building.getAddress()).isEqualTo("c/Building 1");
	}

}
