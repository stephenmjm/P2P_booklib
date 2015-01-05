package io.cgcclub.booklib.web.support;

import org.springframework.http.HttpStatus;

public class RestException extends RuntimeException {

	public HttpStatus status;

	public RestException(String message, HttpStatus status) {
		super(message);
		this.status = status;
	}

}
