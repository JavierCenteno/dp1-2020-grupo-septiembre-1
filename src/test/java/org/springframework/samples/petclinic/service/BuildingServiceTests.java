package org.springframework.samples.petclinic.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;
import java.util.Optional;

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
	void shouldFindAllBuildings() {
		Collection<Building> buildings = this.buildingService.findAll();
		assertThat(buildings.size()).isEqualTo(4);
		Building building1 = EntityUtils.getById(buildings, Building.class, 1);
		assertThat(building1.getName()).isEqualTo("building1");
		assertThat(building1.getAddress()).isEqualTo("c/Building 1");
		Building building2 = EntityUtils.getById(buildings, Building.class, 2);
		assertThat(building2.getName()).isEqualTo("building2");
		assertThat(building2.getAddress()).isEqualTo("c/Building 2");
		Building building3 = EntityUtils.getById(buildings, Building.class, 3);
		assertThat(building3.getName()).isEqualTo("building3");
		assertThat(building3.getAddress()).isEqualTo("c/Building 3");
		Building building4 = EntityUtils.getById(buildings, Building.class, 4);
		assertThat(building4.getName()).isEqualTo("building4");
		assertThat(building4.getAddress()).isEqualTo("c/Building 4");
	}

	@Test
	void shouldFindBuildingWithCorrectId() {
		Optional<Building> building = this.buildingService.findBuildingById(1);
		assertThat(building.get()).isNotNull();
		assertThat(building.get().getName()).isEqualTo("building1");
		assertThat(building.get().getAddress()).isEqualTo("c/Building 1");
	}

}
