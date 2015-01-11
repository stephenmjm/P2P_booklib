package io.cgcclub.booklib.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BookDto {

	@JsonIgnore
	public Long id;

	public String status;

	@JsonProperty("id")
	public String bookId;

	public String title;

	public String url;

	public String image;

	/**
	 * Here are more images for this book, such as large image, medium, small
	 * image...
	 */
	public BookImages images;

	public List<String> author;

	public String summary;

}
