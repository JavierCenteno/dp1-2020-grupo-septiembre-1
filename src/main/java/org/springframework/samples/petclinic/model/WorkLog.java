package org.springframework.samples.petclinic.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Entity
@Table(name = "work_logs")
public class WorkLog extends BaseEntity {

	////////////////////////////////////////////////////////////////////////////////
	// Hours

	@Column(name = "hours")
	@Min(0)
	@Max(8)
	protected Integer hours;

	public Integer getHours() {
		return this.hours;
	}

	public void setHours(Integer hours) {
		this.hours = hours;
	}

	////////////////////////////////////////////////////////////////////////////////
	// Date

	@Column(name = "date")
	protected Date date;

	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	////////////////////////////////////////////////////////////////////////////////
	// Employee

	@ManyToOne
	private Employee employee;

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	////////////////////////////////////////////////////////////////////////////////
	// Task

	@ManyToOne
	private Task task;

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

}
