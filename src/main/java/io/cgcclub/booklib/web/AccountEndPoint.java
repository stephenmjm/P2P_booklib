package io.cgcclub.booklib.web;

import io.cgcclub.booklib.domain.Account;
import io.cgcclub.booklib.repository.AccountDao;
import io.cgcclub.booklib.web.support.RestException;

import java.nio.charset.Charset;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.hash.Hashing;

@RestController
public class AccountEndPoint {

	private static Logger logger = LoggerFactory.getLogger(BookEndpoint.class);

	@Autowired
	private AccountDao accountDao;

	@RequestMapping(value = "/rest/accounts/login")
	public void login(@RequestParam("email") String email, @RequestParam("password") String password,
			HttpSession session) {

		if (StringUtils.isEmpty(email) || StringUtils.isEmpty(password)) {
			throw new RestException("User or password empty", HttpStatus.BAD_REQUEST);
		}

		Account account = accountDao.findByEmail(email);

		if (account == null) {
			throw new RestException("User not exist", HttpStatus.UNAUTHORIZED);
		}

		if (!account.hashPassword.equals(hashPassword(password))) {
			throw new RestException("Password wrong", HttpStatus.UNAUTHORIZED);
		}

		session.setAttribute("account", account);
	}

	@RequestMapping(value = "/rest/accounts/register")
	public void register(@RequestParam("email") String email, @RequestParam("name") String name,
			@RequestParam("password") String password) {

		if (StringUtils.isEmpty(email) || StringUtils.isEmpty(name) || StringUtils.isEmpty(password)) {
			throw new RestException("User or name or password empty", HttpStatus.BAD_REQUEST);
		}

		Account account = new Account();
		account.email = email;
		account.name = name;
		account.hashPassword = hashPassword(password);
		accountDao.save(account);
	}

	public static String hashPassword(String password) {
		return Hashing.sha1().hashString(password, Charset.forName("UTF-8")).toString();
	}
}
