package io.cgcclub.booklib.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
public class Book {

	public static final String STATUS_IDLE = "idle";
	public static final String STATUS_REQUEST = "request";
	public static final String STATUS_OUT = "out";

	@Id
	public Long id;
	public String title;
	public String url;
	public String status;

	@ManyToOne
	@JoinColumn(name = "owner_id")
	public Account owner;

	@JsonFormat(pattern = "yyyy-MM-dd")
	public Date onboardDate;

	@ManyToOne
	@JoinColumn(name = "borrower_id")
	public Account borrower;

	@JsonFormat(pattern = "yyyy-MM-dd")
	public Date borrowDate;
}
