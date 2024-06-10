package com.lenarsharipov.simplebank.mapper;

import com.lenarsharipov.simplebank.dto.client.CreateClientDto;
import com.lenarsharipov.simplebank.model.Account;
import lombok.experimental.UtilityClass;

@UtilityClass
public class AccountMapper {

    public static Account toEntity(CreateClientDto dto) {
        return Account.builder()
                .initialDeposit(dto.getInitialBalance())
                .balance(dto.getInitialBalance())
                .build();
    }
}
