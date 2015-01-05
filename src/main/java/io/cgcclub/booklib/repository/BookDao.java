package io.cgcclub.booklib.repository;

import io.cgcclub.booklib.domain.Book;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface BookDao extends PagingAndSortingRepository<Book, Long> {

	public List<Book> findByOwnerId(Long ownerId);
}
