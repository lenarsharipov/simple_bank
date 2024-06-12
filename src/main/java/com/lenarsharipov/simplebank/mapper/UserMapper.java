package com.lenarsharipov.simplebank.mapper;

import com.lenarsharipov.simplebank.dto.client.CreateClientDto;
import com.lenarsharipov.simplebank.model.Role;
import com.lenarsharipov.simplebank.model.User;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserMapper {

    public static User toEntity(CreateClientDto dto) {
        return User.builder()
                .username(dto.getUsername())
                .password("")
                .role(Role.ROLE_CLIENT)
                .build();
    }
}
