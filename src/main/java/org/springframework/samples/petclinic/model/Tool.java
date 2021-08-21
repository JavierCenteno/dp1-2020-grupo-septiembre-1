package org.springframework.samples.petclinic.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "tools")
public class Tool extends BaseEntity {

	////////////////////////////////////////////////////////////////////////////////
	// Name

	@Column(name = "name")
	@NotBlank
	protected String name;

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	////////////////////////////////////////////////////////////////////////////////
	// Building

	@ManyToOne(optional = false)
	private Building building;

	public Building getBuilding() {
		return building;
	}

	public void setBuilding(Building building) {
		this.building = building;
	}

	////////////////////////////////////////////////////////////////////////////////
	// Task

	@ManyToOne(optional = true)
	private Task task;

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

}
