package io.cgcclub.booklib.web;

import io.cgcclub.booklib.domain.Account;
import io.cgcclub.booklib.domain.Book;
import io.cgcclub.booklib.repository.BookDao;
import io.cgcclub.booklib.service.MailService;
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
	@Autowired
	private MailService mailService;

	@RequestMapping(value = "/rest/books", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
	public Iterable<Book> listAllBook() {
		return bookDao.findAll();
	}

	@RequestMapping(value = "/rest/books", method = RequestMethod.POST, consumes = MediaTypes.JSON_UTF_8)
	public void createBook(@RequestBody Book book, HttpSession session) {
		Account owner = getCurrentAccount(session);
		book.owner = owner;
		book.status = Book.STATUS_IDLE;
		book.onboardDate = new Date();
		bookDao.save(book);
	}

	@RequestMapping(value = "/rest/books/{id}/modify", method = RequestMethod.POST, consumes = MediaTypes.JSON_UTF_8)
	public void modifyBook(@RequestBody Book book, HttpSession session) {
		Account owner = getCurrentAccount(session);
		if (!owner.id.equals(book.owner.id)) {
			throw new RestException("User can't modify others book", HttpStatus.FORBIDDEN);
		}

		Book orginalBook = bookDao.findOne(book.id);
		orginalBook.title = book.title;
		orginalBook.url = book.url;
		bookDao.save(orginalBook);
	}

	@RequestMapping(value = "/rest/books/{id}/delete")
	public void deleteBook(@PathVariable("id") Long id, HttpSession session) {
		Account owner = getCurrentAccount(session);
		Book book = bookDao.findOne(id);

		if (!owner.id.equals(book.owner.id)) {
			throw new RestException("User can't delte others book", HttpStatus.FORBIDDEN);
		}

		bookDao.delete(id);
	}

	@RequestMapping(value = "/rest/books/{id}/request")
	public void applyRequestBook(@PathVariable("id") Long id, HttpSession session) {
		Account borrower = getCurrentAccount(session);
		Book book = bookDao.findOne(id);

		if (!book.status.equals(Book.STATUS_IDLE)) {
			throw new RestException("The book is not idle", HttpStatus.BAD_REQUEST);
		}

		mailService.sendMail(book.owner.email,
				String.format("Request book %s by %s (%s)", book.title, borrower.name, borrower.email),
				"You can go to CGC Book Library for details");

		book.status = Book.STATUS_REQUEST;
		book.borrower = borrower;
		bookDao.save(book);
	}

	@RequestMapping(value = "/rest/books/{id}/cancel")
	public void cancelRequestBook(@PathVariable("id") Long id, HttpSession session) {
		Account borrower = getCurrentAccount(session);
		Book book = bookDao.findOne(id);

		if (!book.status.equals(Book.STATUS_REQUEST)) {
			throw new RestException("The book is not requesting", HttpStatus.BAD_REQUEST);
		}

		if (!borrower.id.equals(book.borrower.id)) {
			throw new RestException("User can't cancel other ones request", HttpStatus.FORBIDDEN);
		}

		mailService.sendMail(book.owner.email,
				String.format("Cancel book %s request by %s (%s)", book.title, borrower.name, borrower.email),
				"You can go to CGC Book Library for details");

		book.status = Book.STATUS_IDLE;
		book.borrower = null;
		bookDao.save(book);
	}

	@RequestMapping(value = "/rest/books/{id}/confirm")
	public void confirmRequestBook(@PathVariable("id") Long id, HttpSession session) {
		Account owner = getCurrentAccount(session);
		Book book = bookDao.findOne(id);

		if (!book.status.equals(Book.STATUS_REQUEST)) {
			throw new RestException("The book is not requesting", HttpStatus.BAD_REQUEST);
		}

		if (!owner.id.equals(book.owner.id)) {
			throw new RestException("User can't cofirm others book", HttpStatus.FORBIDDEN);
		}

		mailService.sendMail(book.borrower.email,
				String.format("Confirm book %s request by %s (%s)", book.title, owner.name, owner.email),
				"You can go to CGC Book Library for details");

		book.status = Book.STATUS_OUT;
		book.borrowDate = new Date();
		bookDao.save(book);
	}

	@RequestMapping(value = "/rest/books/{id}/reject")
	public void rejectRequestBook(@PathVariable("id") Long id, HttpSession session) {
		Account owner = getCurrentAccount(session);
		Book book = bookDao.findOne(id);

		if (!book.status.equals(Book.STATUS_REQUEST)) {
			throw new RestException("The book is not requesting", HttpStatus.BAD_REQUEST);
		}

		if (!owner.id.equals(book.owner.id)) {
			throw new RestException("User can't cofirm others book", HttpStatus.FORBIDDEN);
		}

		mailService.sendMail(book.borrower.email,
				String.format("Reject book %s request by %s (%s)", book.title, owner.name, owner.email),
				"You can go to CGC Book Library for details");

		book.status = Book.STATUS_IDLE;
		book.borrowDate = null;
		book.borrower = null;
		bookDao.save(book);
	}

	@RequestMapping(value = "/rest/books/{id}/return")
	public void returnBook(@PathVariable("id") Long id, HttpSession session) {
		Account owner = getCurrentAccount(session);
		Book book = bookDao.findOne(id);

		if (!owner.id.equals(book.owner.id)) {
			throw new RestException("User can't cofirm others book", HttpStatus.FORBIDDEN);
		}

		if (!book.status.equals(Book.STATUS_OUT)) {
			throw new RestException("The book is not borrowing", HttpStatus.BAD_REQUEST);
		}

		mailService.sendMail(book.borrower.email,
				String.format("Mark book %s returned by %s (%s)", book.title, owner.name, owner.email),
				"You can go to CGC Book Library for details");

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
