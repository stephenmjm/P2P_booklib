package io.cgcclub.booklib.repository;

import io.cgcclub.booklib.domain.Book;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface BookDao extends PagingAndSortingRepository<Book, Long> {
}
