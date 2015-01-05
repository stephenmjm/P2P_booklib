package io.cgcclub.booklib.web;

import io.cgcclub.booklib.domain.Account;
import io.cgcclub.booklib.repository.AccountDao;
import io.cgcclub.booklib.web.support.RestException;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountEndPoint {

	@Autowired
	private AccountDao accountDao;

	@RequestMapping(value = "/rest/login")
	public void login(@RequestParam("email") String email, @RequestParam("password") String password,
			HttpSession session) {
		Account account = accountDao.findByEmail(email);

		if (account != null) {
			session.setAttribute("account", account);
		} else {
			throw new RestException("User not exist", HttpStatus.UNAUTHORIZED);
		}
	}
}
