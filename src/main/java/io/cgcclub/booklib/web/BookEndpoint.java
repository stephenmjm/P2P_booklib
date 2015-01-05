package io.cgcclub.booklib.web;

import io.cgcclub.booklib.domain.Book;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;

@RestController
public class BookEndpoint {

	public static final String JSON_UTF_8 = "application/json; charset=UTF-8";

	@RequestMapping(value = "/rest/books", produces = JSON_UTF_8)
	public List<Book> listAll() {
		Book book = new Book();
		book.name = "Big Data";
		book.url = "http://book.douban.com/subject/25984046/";
		book.ownerId = 1L;
		return Lists.newArrayList(book);
	}
}
