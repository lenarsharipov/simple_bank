package com.lenarsharipov.simplebank.mapper;

import com.lenarsharipov.simplebank.dto.email.CreatedEmailDto;
import com.lenarsharipov.simplebank.model.Email;
import lombok.experimental.UtilityClass;

@UtilityClass
public class EmailMapper {

    public static CreatedEmailDto toDto(Email email) {
        return CreatedEmailDto.builder()
                .email(email.getAddress())
                .build();
    }
}
