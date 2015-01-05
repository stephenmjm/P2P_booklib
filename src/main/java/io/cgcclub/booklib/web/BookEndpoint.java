package io.cgcclub.booklib.web;

import io.cgcclub.booklib.domain.Account;
import io.cgcclub.booklib.domain.Book;
import io.cgcclub.booklib.repository.BookDao;
import io.cgcclub.booklib.web.support.MediaTypes;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookEndpoint {

	@Autowired
	private BookDao bookDao;

	@RequestMapping(value = "/rest/books", produces = MediaTypes.JSON_UTF_8)
	public Iterable<Book> listAllBook() {
		return bookDao.findAll();
	}

	@RequestMapping(value = "/rest/mybook", produces = MediaTypes.JSON_UTF_8)
	public Iterable<Book> listMyBook(HttpSession session) {
		Account account = (Account) session.getAttribute("account");
		return bookDao.findByOwnerId(account.id);
	}
}
