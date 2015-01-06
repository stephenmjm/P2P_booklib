package io.cgcclub.booklib.service.impl;

import io.cgcclub.booklib.service.entry.BookInfo;

import java.io.IOException;

import org.apache.http.client.fluent.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class BookInfoServiceDoubanImpl {

	private static final String DOUBAN_URL = "https://api.douban.com/v2/book/%s?fields=id,title,url,image,author,summary";

	private static final ObjectMapper MAPPER = new ObjectMapper();

	private static final Logger logger = LoggerFactory
			.getLogger(BookInfoServiceDoubanImpl.class);

	public BookInfo findBookInfo(String doubanBookId) {
		String doubanQueryRequestUrl = String.format(DOUBAN_URL, doubanBookId);
		try {
			String bookInfoJsonString = Request.Get(doubanQueryRequestUrl)
					.execute().returnContent().asString();
			return MAPPER.readValue(bookInfoJsonString, BookInfo.class);
		} catch (IOException e) {
			logger.error(
					"Failed to retrieve book info from douban for bookId: {}.",
					doubanBookId);
		}

		return null;
	}
}
