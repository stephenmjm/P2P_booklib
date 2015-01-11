package io.cgcclub.booklib.service.book;

import io.cgcclub.booklib.domain.Book;
import io.cgcclub.booklib.dto.BookDto;
import io.cgcclub.booklib.repository.BookDao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

@Component
public class BookService {

	@Autowired
	private BookInfoProviderProxy bookInfoProvider;

	@Autowired
	private BookDao bookDao;

	public List<BookDto> findAll() {
		List<BookDto> bookDtoList = Lists.newArrayList();
		Iterable<Book> books = bookDao.findAll();

		for (Book book : books) {
			bookDtoList.add(getBookDtoByBookId(book.bookId));
		}

		return bookDtoList;
	}

	private BookDto getBookDtoByBookId(String bookId) {
		return bookInfoProvider.findBookInfo(bookId);
	}
}
