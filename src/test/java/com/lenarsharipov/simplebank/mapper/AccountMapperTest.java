package com.lenarsharipov.simplebank.mapper;

import com.lenarsharipov.simplebank.dto.client.CreateClientDto;
import com.lenarsharipov.simplebank.model.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class AccountMapperTest {

    private AccountMapper accountMapper;
    private static final int INITIAL_BALANCE = 100;

    @BeforeEach
    public void setup() {
        accountMapper = Mappers.getMapper(AccountMapper.class);
    }

    @Test
    public void testToAccountEntity() {
        var dto = CreateClientDto.builder()
                .initialBalance(new BigDecimal(INITIAL_BALANCE))
                .build();

        Account actualAccount = accountMapper.toAccountEntity(dto);

        assertNull(actualAccount.getId());
        assertEquals(actualAccount.getVersion(), 0);
        assertEquals(new BigDecimal(INITIAL_BALANCE), actualAccount.getInitialDeposit());
        assertEquals(new BigDecimal(INITIAL_BALANCE), actualAccount.getBalance());
    }

}