package com.lenarsharipov.simplebank.mapper;

import com.lenarsharipov.simplebank.dto.phone.CreatedPhoneDto;
import com.lenarsharipov.simplebank.model.Phone;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PhoneMapper {

    public static CreatedPhoneDto toDto(Phone phone) {
        return CreatedPhoneDto.builder()
                .phone(phone.getNumber())
                .build();
    }
}