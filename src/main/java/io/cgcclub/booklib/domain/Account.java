package io.cgcclub.booklib.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Account {
	@Id
	public Long id;
	public String email;
	public String name;

	@JsonIgnore
	public String hashPassword;

	@Override
	public String toString() {
		return "Account [id=" + id + ", email=" + email + ", name=" + name
				+ ", hashPassword=" + hashPassword + "]";
	}
}
