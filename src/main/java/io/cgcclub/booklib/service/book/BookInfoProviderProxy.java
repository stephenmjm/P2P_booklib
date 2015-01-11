package io.cgcclub.booklib.service.book;

import io.cgcclub.booklib.dto.BookDto;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

@Component
public class BookInfoProviderProxy {

	private static Logger logger = LoggerFactory
			.getLogger(BookInfoProviderProxy.class);

	@Autowired
	private BookInfoProviderDoubanImpl bookInfoServiceDoubanImpl;

	private LoadingCache<String, BookDto> bookInfoCache;

	@PostConstruct
	public void init() {
		bookInfoCache = CacheBuilder.newBuilder().initialCapacity(50)
				.maximumSize(1000).expireAfterWrite(7, TimeUnit.DAYS)
				.build(new CacheLoader<String, BookDto>() {

					@Override
					public BookDto load(String key) throws Exception {
						return bookInfoServiceDoubanImpl.findBookInfo(key);
					}
				});
	}

	public BookDto findBookInfo(String bookId) {
		try {
			return bookInfoCache.get(bookId);
		} catch (ExecutionException e) {
			logger.error("Failed to get book info for bookId: {}.", bookId,
					e.getCause());
			throw new RuntimeException("Failed to get book info.");
		}
	}
}
