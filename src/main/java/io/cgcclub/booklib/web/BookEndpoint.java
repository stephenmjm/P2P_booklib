package io.cgcclub.booklib.web;

import io.cgcclub.booklib.domain.Account;
import io.cgcclub.booklib.domain.Book;
import io.cgcclub.booklib.repository.BookDao;
import io.cgcclub.booklib.web.support.MediaTypes;

import java.util.Date;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookEndpoint {

	@Autowired
	private BookDao bookDao;

	@RequestMapping(value = "/rest/books", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
	public Iterable<Book> listAllBook() {
		return bookDao.findAll();
	}

	@RequestMapping(value = "/rest/books", method = RequestMethod.POST, consumes = MediaTypes.JSON_UTF_8)
	public void createBook(@RequestBody Book book, HttpSession session) {
		Account account = getCurrentAccount(session);
		book.owner = account;
		book.status = Book.STATUS_IDLE;
		book.onboardDate = new Date();
		bookDao.save(book);
	}

	@RequestMapping(value = "/rest/mybook", produces = MediaTypes.JSON_UTF_8)
	public Iterable<Book> listMyBook(HttpSession session) {
		Account account = getCurrentAccount(session);
		return bookDao.findByOwnerId(account.id);
	}

	private Account getCurrentAccount(HttpSession session) {
		return (Account) session.getAttribute("account");
	}
}
