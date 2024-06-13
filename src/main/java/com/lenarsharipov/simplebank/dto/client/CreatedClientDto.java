package com.lenarsharipov.simplebank.dto.client;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record CreatedClientDto(
    String username,
    String fullName,
    LocalDate birthDate,
    String accountNo,
    String email,
    String phone) {
}
