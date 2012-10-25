package com.goodow.web.core.shared;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlType;

@XmlType
@Entity
@Table(name = "t_user")
public class User extends WebContent {

	@NotNull(message = "你必须指定用户名")
	private String name;

	private String email;

	private String phone;

	private String state;

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	private String zip;

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public String getPhone() {
		return phone;
	}

	public void setName(final String userName) {
		this.name = userName;
	}

	public User setEmail(final String password) {
		this.email = password;
		return this;
	}

	public User setPhone(final String passwordSalt) {
		this.phone = passwordSalt;
		return this;
	}

}
