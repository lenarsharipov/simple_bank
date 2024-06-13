package com.lenarsharipov.simplebank.dto.filter;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public record ClientFiltersDto(
        String phone,
        String email,
        String fullName,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate birthDate) {
}