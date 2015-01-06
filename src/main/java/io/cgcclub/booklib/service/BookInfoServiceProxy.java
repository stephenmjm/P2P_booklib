package io.cgcclub.booklib.service;

import io.cgcclub.booklib.service.entry.BookInfo;
import io.cgcclub.booklib.service.impl.BookInfoServiceDoubanImpl;

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
public class BookInfoServiceProxy {

	private static final Logger logger = LoggerFactory
			.getLogger(BookInfoServiceProxy.class);

	@Autowired
	private BookInfoServiceDoubanImpl bookInfoServiceDoubanImpl;

	private LoadingCache<String, BookInfo> bookInfoCache;

	@PostConstruct
	public void init() {
		bookInfoCache = (LoadingCache<String, BookInfo>) CacheBuilder
				.newBuilder().initialCapacity(50).maximumSize(200)
				.expireAfterWrite(7, TimeUnit.DAYS)
				.build(new CacheLoader<String, BookInfo>() {

					@Override
					public BookInfo load(String key) throws Exception {
						return bookInfoServiceDoubanImpl.findBookInfo(key);
					}
				});
	}

	public BookInfo findBookInfo(String bookId){
		try {
			return bookInfoCache.get(bookId);
		} catch (ExecutionException e) {
			logger.error("Failed to get book info for bookId: {}.", bookId,
					e.getCause());
			throw new RuntimeException("Failed to get book info.");
		}
	}
}
