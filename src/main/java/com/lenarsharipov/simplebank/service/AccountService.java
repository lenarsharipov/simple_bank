package com.lenarsharipov.simplebank.service;

import com.lenarsharipov.simplebank.model.Account;
import com.lenarsharipov.simplebank.repository.AccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class AccountService {

    private final AccountRepository accountRepository;

    @Transactional
    public Account create(BigDecimal initialBalance) {
        Account account = Account.builder()
                .initialDeposit(initialBalance)
                .balance(initialBalance)
                .build();
        accountRepository.save(account);
        return account;
    }
}
