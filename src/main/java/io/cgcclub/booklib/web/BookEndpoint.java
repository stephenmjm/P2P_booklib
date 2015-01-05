package io.cgcclub.booklib.web;

import io.cgcclub.booklib.domain.Book;
import io.cgcclub.booklib.repository.BookDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookEndpoint {

	public static final String JSON_UTF_8 = "application/json; charset=UTF-8";

	@Autowired
	private BookDao bookDao;

	@RequestMapping(value = "/rest/books", produces = JSON_UTF_8)
	public Iterable<Book> listAll() {
		return bookDao.findAll();
	}
}
