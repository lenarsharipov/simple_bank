package com.lenarsharipov.simplebank.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

@Value
@Builder
@AllArgsConstructor
public class UserFiltersDto {

    String phone;
    String fullName;
    String email;
    LocalDate birthDate;
}
