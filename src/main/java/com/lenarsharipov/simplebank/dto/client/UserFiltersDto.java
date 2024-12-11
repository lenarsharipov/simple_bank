package com.lenarsharipov.simplebank.dto.client;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record UserFiltersDto(
    String phone,
    String fullName,
    String email,
    LocalDate birthDate) {
}
