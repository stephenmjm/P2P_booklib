package io.cgcclub.booklib.repository;

import io.cgcclub.booklib.domain.Account;

import org.springframework.data.repository.CrudRepository;

public interface AccountDao extends CrudRepository<Account, Long> {

	Account findByEmail(String email);
}
