package org.springframework.samples.petclinic.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Tool;
import org.springframework.samples.petclinic.util.EntityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
class ToolServiceTests {

	////////////////////////////////////////////////////////////////////////////////
	// Services

	@Autowired
	protected ToolService toolService;

	////////////////////////////////////////////////////////////////////////////////
	// Tests

	@Test
	void shouldFindAllTools() {
		Collection<Tool> tools = this.toolService.findAll();
		assertThat(tools.size()).isEqualTo(4);
		Tool tool1 = EntityUtils.getById(tools, Tool.class, 1);
		assertThat(tool1.getName()).isEqualTo("tool1");
		Tool tool2 = EntityUtils.getById(tools, Tool.class, 2);
		assertThat(tool2.getName()).isEqualTo("tool2");
		Tool tool3 = EntityUtils.getById(tools, Tool.class, 3);
		assertThat(tool3.getName()).isEqualTo("tool3");
		Tool tool4 = EntityUtils.getById(tools, Tool.class, 4);
		assertThat(tool4.getName()).isEqualTo("tool4");
	}

	@Test
	void shouldFindToolWithCorrectId() {
		Optional<Tool> tool = this.toolService.findToolById(1);
		assertThat(tool.get()).isNotNull();
		assertThat(tool.get().getName()).isEqualTo("tool1");
	}

	@Test
	@Transactional
	public void shouldInsertToolIntoDatabaseAndGenerateId() {
		Tool tool = new Tool();
		tool.setName("tool5");
		this.toolService.saveTool(tool);
		assertThat(tool.getId()).isNotNull();
	}

}
