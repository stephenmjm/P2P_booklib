package io.cgcclub.booklib.web;

import io.cgcclub.booklib.domain.Account;
import io.cgcclub.booklib.domain.Book;
import io.cgcclub.booklib.repository.BookDao;
import io.cgcclub.booklib.web.support.MediaTypes;
import io.cgcclub.booklib.web.support.RestException;

import java.util.Date;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
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

	@RequestMapping(value = "/rest/books/{id}/modify", method = RequestMethod.POST, consumes = MediaTypes.JSON_UTF_8)
	public void modifyBook(@RequestBody Book book, HttpSession session) {
		Account account = getCurrentAccount(session);
		if (!account.id.equals(book.owner.id)) {
			throw new RestException("User can't modify others book", HttpStatus.FORBIDDEN);
		}

		Book orginalBook = bookDao.findOne(book.id);
		orginalBook.title = book.title;
		orginalBook.url = book.url;
		bookDao.save(orginalBook);
	}

	@RequestMapping(value = "/rest/books/{id}/delete", method = RequestMethod.GET, consumes = MediaTypes.JSON_UTF_8)
	public void deleteBook(@PathVariable("id") Long id, HttpSession session) {
		Account account = getCurrentAccount(session);
		Book book = bookDao.findOne(id);

		if (!account.id.equals(book.owner.id)) {
			throw new RestException("User can't delte others book", HttpStatus.FORBIDDEN);
		}

		bookDao.delete(id);
	}

	@RequestMapping(value = "/rest/books/{id}/request", method = RequestMethod.GET, consumes = MediaTypes.JSON_UTF_8)
	public void applyRequestBook(@PathVariable("id") Long id, HttpSession session) {
		Account account = getCurrentAccount(session);
		Book book = bookDao.findOne(id);

		if (!book.status.equals(Book.STATUS_IDLE)) {
			throw new RestException("The book is not idle", HttpStatus.BAD_REQUEST);
		}

		book.status = Book.STATUS_REQUEST;
		book.borrower = account;
		// TODO: send notify mail
		bookDao.save(book);
	}

	@RequestMapping(value = "/rest/books/{id}/cancel", method = RequestMethod.GET, consumes = MediaTypes.JSON_UTF_8)
	public void cancelRequestBook(@PathVariable("id") Long id, HttpSession session) {
		Account account = getCurrentAccount(session);
		Book book = bookDao.findOne(id);

		if (!book.status.equals(Book.STATUS_REQUEST)) {
			throw new RestException("The book is not requesting", HttpStatus.BAD_REQUEST);
		}

		if (!account.id.equals(book.borrower.id)) {
			throw new RestException("User can't cancel other ones request", HttpStatus.FORBIDDEN);
		}

		book.status = Book.STATUS_IDLE;
		book.borrower = null;
		bookDao.save(book);
		// TODO: send notify mail
	}

	@RequestMapping(value = "/rest/books/{id}/confirm", method = RequestMethod.GET, consumes = MediaTypes.JSON_UTF_8)
	public void confirmRequestBook(@PathVariable("id") Long id, HttpSession session) {
		Account account = getCurrentAccount(session);
		Book book = bookDao.findOne(id);

		if (!book.status.equals(Book.STATUS_REQUEST)) {
			throw new RestException("The book is not requesting", HttpStatus.BAD_REQUEST);
		}

		if (!account.id.equals(book.owner.id)) {
			throw new RestException("User can't cofirm others book", HttpStatus.FORBIDDEN);
		}

		book.status = Book.STATUS_OUT;
		book.borrowDate = new Date();
		bookDao.save(book);
		// TODO: send notify mail
	}

	@RequestMapping(value = "/rest/books/{id}/reject", method = RequestMethod.GET, consumes = MediaTypes.JSON_UTF_8)
	public void rejectRequestBook(@PathVariable("id") Long id, HttpSession session) {
		Account account = getCurrentAccount(session);
		Book book = bookDao.findOne(id);

		if (!book.status.equals(Book.STATUS_REQUEST)) {
			throw new RestException("The book is not requesting", HttpStatus.BAD_REQUEST);
		}

		if (!account.id.equals(book.owner.id)) {
			throw new RestException("User can't cofirm others book", HttpStatus.FORBIDDEN);
		}

		book.status = Book.STATUS_IDLE;
		book.borrowDate = null;
		book.borrower = null;
		bookDao.save(book);
		// TODO: send notify mail
	}

	@RequestMapping(value = "/rest/books/{id}/return", method = RequestMethod.GET, consumes = MediaTypes.JSON_UTF_8)
	public void returnBook(@PathVariable("id") Long id, HttpSession session) {
		Account account = getCurrentAccount(session);
		Book book = bookDao.findOne(id);

		if (!account.id.equals(book.owner.id)) {
			throw new RestException("User can't cofirm others book", HttpStatus.FORBIDDEN);
		}

		if (!book.status.equals(Book.STATUS_OUT)) {
			throw new RestException("The book is not borrowing", HttpStatus.BAD_REQUEST);
		}

		book.status = Book.STATUS_IDLE;
		book.borrowDate = null;
		book.borrower = null;
		bookDao.save(book);
	}

	@RequestMapping(value = "/rest/mybook", produces = MediaTypes.JSON_UTF_8)
	public Iterable<Book> listMyBook(HttpSession session) {
		Account account = getCurrentAccount(session);
		return bookDao.findByOwnerId(account.id);
	}

	@RequestMapping(value = "/rest/myborrowedbook", produces = MediaTypes.JSON_UTF_8)
	public Iterable<Book> listMyBorrowedBook(HttpSession session) {
		Account account = getCurrentAccount(session);
		return bookDao.findByBorrowerId(account.id);
	}

	private Account getCurrentAccount(HttpSession session) {
		Account account = (Account) session.getAttribute("account");
		if (account == null) {
			throw new RestException("User doesn't login", HttpStatus.UNAUTHORIZED);
		}
		return account;
	}
}
