package org.springframework.samples.petclinic.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "buildings")
public class Building extends BaseEntity {

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
	// Address

	@Column(name = "address")
	@NotBlank
	protected String address;

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	////////////////////////////////////////////////////////////////////////////////
	// Income

	@Column(name = "income")
	@Min(0)
	protected Integer income;

	public Integer getIncome() {
		return this.income;
	}

	public void setIncome(Integer income) {
		this.income = income;
	}

	////////////////////////////////////////////////////////////////////////////////
	// Employee

	@OneToMany(mappedBy = "building")
	private Set<Employee> employees;

	protected Set<Employee> getEmployeesInternal() {
		if (this.employees == null) {
			this.employees = new HashSet<>();
		}
		return this.employees;
	}

	protected void setEmployeesInternal(Set<Employee> employees) {
		this.employees = employees;
	}

	public List<Employee> getEmployees() {
		List<Employee> sortedEmployees = new ArrayList<>(getEmployeesInternal());
		return Collections.unmodifiableList(sortedEmployees);
	}

	public void addEmployee(Employee employee) {
		getEmployeesInternal().add(employee);
	}

	public boolean removeEmployee(Employee employee) {
		employee.setBuilding(null);
		return getEmployeesInternal().remove(employee);
	}

	////////////////////////////////////////////////////////////////////////////////
	// Tool

	@OneToMany(mappedBy = "building")
	private Set<Tool> tools;

	protected Set<Tool> getToolsInternal() {
		if (this.tools == null) {
			this.tools = new HashSet<>();
		}
		return this.tools;
	}

	protected void setToolsInternal(Set<Tool> tools) {
		this.tools = tools;
	}

	public List<Tool> getTools() {
		List<Tool> sortedTools = new ArrayList<>(getToolsInternal());
		return Collections.unmodifiableList(sortedTools);
	}

	public void addTool(Tool tool) {
		getToolsInternal().add(tool);
	}

	public boolean removeTool(Tool tool) {
		tool.setBuilding(null);
		return getToolsInternal().remove(tool);
	}

}
