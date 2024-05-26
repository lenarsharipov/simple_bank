package com.lenarsharipov.simplebank.repository;

import com.lenarsharipov.simplebank.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
