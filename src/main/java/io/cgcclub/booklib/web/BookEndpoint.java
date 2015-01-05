package io.cgcclub.booklib.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookEndpoint {

	@RequestMapping(value = "/rest/books")
	public String listAll() {
		return "Wellcome to CGC Book Library";
	}
}
