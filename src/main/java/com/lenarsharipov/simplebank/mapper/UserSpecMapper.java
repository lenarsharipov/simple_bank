package com.lenarsharipov.simplebank.mapper;

import com.lenarsharipov.simplebank.dto.filter.FiltersDto;
import com.lenarsharipov.simplebank.service.search.UserSpecification;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserSpecMapper {

    public static UserSpecification toSpecification(FiltersDto dto) {
        return UserSpecification.builder()
                .phone(dto.getPhone())
                .fullName(dto.getFullName())
                .email(dto.getEmail())
                .birthDate(dto.getBirthDate())
                .build();
    }
}
