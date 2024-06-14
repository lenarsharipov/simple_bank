package com.lenarsharipov.simplebank.mapper;

import com.lenarsharipov.simplebank.dto.filter.ClientFiltersDto;
import com.lenarsharipov.simplebank.service.search.ClientSpecification;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ClientSpecMapperManual {

    public static ClientSpecification toSpecification(ClientFiltersDto dto) {
        return ClientSpecification.builder()
                .phone(dto.phone())
                .fullName(dto.fullName())
                .email(dto.email())
                .birthDate(dto.birthDate())
                .build();
    }
}
