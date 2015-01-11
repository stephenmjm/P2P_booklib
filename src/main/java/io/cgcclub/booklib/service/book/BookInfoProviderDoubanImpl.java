package io.cgcclub.booklib.service.book;

import io.cgcclub.booklib.dto.BookDto;

import java.io.IOException;

import org.apache.http.client.fluent.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class BookInfoProviderDoubanImpl {

	private static final String DOUBAN_URL = "https://api.douban.com/v2/book/%s?fields=id,title,url,image,images,author,summary";

	private static Logger logger = LoggerFactory.getLogger(BookInfoProviderDoubanImpl.class);

	private static ObjectMapper jsonMapper = new ObjectMapper();

	public BookDto findBookInfo(String doubanBookId) {
		String doubanQueryRequestUrl = String.format(DOUBAN_URL, doubanBookId);
		try {
			String bookInfoJsonString = Request.Get(doubanQueryRequestUrl).execute().returnContent().asString();
			return jsonMapper.readValue(bookInfoJsonString, BookDto.class);
		} catch (IOException e) {
			logger.error("Failed to retrieve book info from douban for bookId: " + doubanBookId + ".", e);
		}

		return null;
	}
}
