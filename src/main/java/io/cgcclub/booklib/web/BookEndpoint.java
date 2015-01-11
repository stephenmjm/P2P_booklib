package io.cgcclub.booklib.web;

import io.cgcclub.booklib.domain.Account;
import io.cgcclub.booklib.domain.Book;
import io.cgcclub.booklib.dto.BookDto;
import io.cgcclub.booklib.repository.BookDao;
import io.cgcclub.booklib.service.MailService;
import io.cgcclub.booklib.service.book.BookService;
import io.cgcclub.booklib.web.support.MediaTypes;
import io.cgcclub.booklib.web.support.RestException;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookEndpoint {

	private static final String MAIL_CONTENT = "You can go to CGC Book Library for details http://cgcbooklib.herokuapp.com/ ";

	private static Logger logger = LoggerFactory.getLogger(BookEndpoint.class);

	@Autowired
	private BookDao bookDao;

	@Autowired
	private BookService bookService;

	@Autowired
	private MailService mailService;

	@RequestMapping(value = "/rest/books", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
	public List<BookDto> listAllBook() {
		return bookService.findAll();
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
			throw new RestException("User can't delete others book", HttpStatus.FORBIDDEN);
		}

		bookDao.delete(id);
	}

	@RequestMapping(value = "/rest/books/{id}/request")
	public void applyBorrowRequest(@PathVariable("id") Long id, HttpSession session) {
		Account borrower = getCurrentAccount(session);
		Book book = bookDao.findOne(id);

		if (!book.status.equals(Book.STATUS_IDLE)) {
			throw new RestException("The book is not idle", HttpStatus.BAD_REQUEST);
		}

		if (borrower.id.equals(book.owner.id)) {
			throw new RestException("User shouldn't borrower the book which is himeself", HttpStatus.FORBIDDEN);
		}

		mailService.sendMail(book.owner.email, borrower.email,
				String.format("Request book <%s> by %s", book.title, borrower.name), MAIL_CONTENT);

		book.status = Book.STATUS_REQUEST;
		book.borrower = borrower;
		bookDao.save(book);
	}

	@RequestMapping(value = "/rest/books/{id}/cancel")
	public void cancelBorrowRequest(@PathVariable("id") Long id, HttpSession session) {
		Account borrower = getCurrentAccount(session);
		Book book = bookDao.findOne(id);

		if (!book.status.equals(Book.STATUS_REQUEST)) {
			throw new RestException("The book is not requesting", HttpStatus.BAD_REQUEST);
		}

		if (!borrower.id.equals(book.borrower.id)) {
			throw new RestException("User can't cancel other ones request", HttpStatus.FORBIDDEN);
		}

		mailService.sendMail(book.owner.email, borrower.email,
				String.format("Cancel book <%s> request by %s", book.title, borrower.name), MAIL_CONTENT);

		book.status = Book.STATUS_IDLE;
		book.borrower = null;
		bookDao.save(book);
	}

	@RequestMapping(value = "/rest/books/{id}/confirm")
	public void markBookBorrowed(@PathVariable("id") Long id, HttpSession session) {
		Account owner = getCurrentAccount(session);
		Book book = bookDao.findOne(id);

		if (!book.status.equals(Book.STATUS_REQUEST)) {
			throw new RestException("The book is not requesting", HttpStatus.BAD_REQUEST);
		}

		if (!owner.id.equals(book.owner.id)) {
			throw new RestException("User can't cofirm others book", HttpStatus.FORBIDDEN);
		}

		mailService.sendMail(book.borrower.email, owner.email,
				String.format("Confirm book <%s> request by %s", book.title, owner.name), MAIL_CONTENT);

		book.status = Book.STATUS_OUT;
		book.borrowDate = new Date();
		bookDao.save(book);
	}

	@RequestMapping(value = "/rest/books/{id}/reject")
	public void rejectBorrowRequest(@PathVariable("id") Long id, HttpSession session) {
		Account owner = getCurrentAccount(session);
		Book book = bookDao.findOne(id);

		if (!book.status.equals(Book.STATUS_REQUEST)) {
			throw new RestException("The book is not requesting", HttpStatus.BAD_REQUEST);
		}

		if (!owner.id.equals(book.owner.id)) {
			throw new RestException("User can't reject others book", HttpStatus.FORBIDDEN);
		}

		mailService.sendMail(book.borrower.email, owner.email,
				String.format("Reject book <%s> request by %s", book.title, owner.name), MAIL_CONTENT);

		book.status = Book.STATUS_IDLE;
		book.borrowDate = null;
		book.borrower = null;
		bookDao.save(book);
	}

	@RequestMapping(value = "/rest/books/{id}/return")
	public void markBookReturned(@PathVariable("id") Long id, HttpSession session) {
		Account owner = getCurrentAccount(session);
		Book book = bookDao.findOne(id);

		if (!book.status.equals(Book.STATUS_OUT)) {
			throw new RestException("The book is not borrowing", HttpStatus.BAD_REQUEST);
		}

		if (!owner.id.equals(book.owner.id)) {
			throw new RestException("User can't make others book returned", HttpStatus.FORBIDDEN);
		}

		mailService.sendMail(book.borrower.email, owner.email,
				String.format("Mark book <%s> returned by %s", book.title, owner.name), MAIL_CONTENT);

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
