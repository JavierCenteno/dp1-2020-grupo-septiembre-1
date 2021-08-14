package org.springframework.samples.petclinic.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;

@Entity
@Table(name = "employees")
public class Employee extends Actor {

	////////////////////////////////////////////////////////////////////////////////
	// User

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "username", referencedColumnName = "username")
	private User user;

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	////////////////////////////////////////////////////////////////////////////////
	// Building

	@ManyToOne
	private Building building;

	public Building getBuilding() {
		return building;
	}

	public void setBuilding(Building building) {
		this.building = building;
	}

	////////////////////////////////////////////////////////////////////////////////
	// Task

	@ManyToMany(mappedBy = "employees")
	private Set<Task> tasks;

	protected Set<Task> getTasksInternal() {
		if (this.tasks == null) {
			this.tasks = new HashSet<>();
		}
		return this.tasks;
	}

	protected void setTasksInternal(Set<Task> tasks) {
		this.tasks = tasks;
	}

	public List<Task> getTasks() {
		List<Task> sortedTasks = new ArrayList<>(getTasksInternal());
		PropertyComparator.sort(sortedTasks, new MutableSortDefinition("name", true, true));
		return Collections.unmodifiableList(sortedTasks);
	}

	public void addTask(Task task) {
		getTasksInternal().add(task);
	}

	public boolean removeTask(Task task) {
		return getTasksInternal().remove(task);
	}

	////////////////////////////////////////////////////////////////////////////////
	// WorkLog

	@OneToMany(mappedBy = "employee")
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
		return getWorkLogsInternal().remove(workLog);
	}

}
