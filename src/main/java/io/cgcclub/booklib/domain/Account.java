package io.cgcclub.booklib.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Account {
	@Id
	public Long id;
	public String email;
	public String name;
	public String hashPassword;
}
