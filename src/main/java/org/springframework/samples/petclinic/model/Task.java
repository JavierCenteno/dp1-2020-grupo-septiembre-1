package org.springframework.samples.petclinic.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;

@Entity
@Table(name = "tasks")
public class Task extends BaseEntity {

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
	// Complete

	@Column(name = "complete")
	protected Boolean complete;

	public Boolean getComplete() {
		return this.complete;
	}

	public void setComplete(Boolean complete) {
		this.complete = complete;
	}

	////////////////////////////////////////////////////////////////////////////////
	// Employee

	@ManyToMany
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
		PropertyComparator.sort(sortedEmployees, new MutableSortDefinition("name", true, true));
		return Collections.unmodifiableList(sortedEmployees);
	}

	public void addEmployee(Employee employee) {
		getEmployeesInternal().add(employee);
	}

	public boolean removeEmployee(Employee employee) {
		return getEmployeesInternal().remove(employee);
	}

	////////////////////////////////////////////////////////////////////////////////
	// Tool

	@OneToMany(mappedBy = "task")
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
		tool.setTask(null);
		return getToolsInternal().remove(tool);
	}

	////////////////////////////////////////////////////////////////////////////////
	// WorkLog

	@OneToMany(mappedBy = "task")
	private Set<WorkLog> workLogs;

	protected Set<WorkLog> getWorkLogsInternal() {
		if (this.workLogs == null) {
			this.workLogs = new HashSet<>();
		}
		return this.workLogs;
	}

	protected void setWorkLogsInternal(Set<WorkLog> workLogs) {
		this.workLogs = workLogs;
	}

	public List<WorkLog> getWorkLogs() {
		List<WorkLog> sortedWorkLogs = new ArrayList<>(getWorkLogsInternal());
		return Collections.unmodifiableList(sortedWorkLogs);
	}

	public void addWorkLog(WorkLog workLog) {
		getWorkLogsInternal().add(workLog);
	}

	public boolean removeWorkLog(WorkLog workLog) {
		workLog.setTask(null);
		return getWorkLogsInternal().remove(workLog);
	}

}
