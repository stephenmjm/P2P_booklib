package io.cgcclub.booklib.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class MailService {

	@Autowired
	private JavaMailSender mailSender;

	public void sendMail(String to, String subject, String content) {
		SimpleMailMessage mail = new SimpleMailMessage();
		mail.setFrom("cgcbooklib@163.com");
		mail.setTo(to);
		mail.setSubject(subject);
		mail.setText(content);
		mailSender.send(mail);
	}
}
