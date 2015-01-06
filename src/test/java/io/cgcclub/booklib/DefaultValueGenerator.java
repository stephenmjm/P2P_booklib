package io.cgcclub.booklib;

import io.cgcclub.booklib.web.AccountEndPoint;

public class DefaultValueGenerator {

	public static void main(String[] args) {
		System.out.println(AccountEndPoint.hashPassword("123"));
	}

}
