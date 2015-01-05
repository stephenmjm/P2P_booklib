package io.cgcclub.booklib.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Book {

	public static final String STATUS_IDLE = "idle";
	public static final String STATUS_REQUEST = "request";
	public static final String STATUS_OUT = "out";

	@Id
	public Long id;
	public String title;
	public String url;
	@ManyToOne
	@JoinColumn(name = "owner_id")
	public Account owner;
	public Long onboardDate;

	public String status;
	@ManyToOne
	@JoinColumn(name = "borrower_id")
	public Account borrower;
	public Long borrowDate;
}
