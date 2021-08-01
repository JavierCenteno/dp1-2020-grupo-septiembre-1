package org.springframework.samples.petclinic.model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@MappedSuperclass
public class Actor extends BaseEntity {

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
	// Email

	@Column(name = "email")
	@NotBlank
	@Email
	protected String email;

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
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

}
