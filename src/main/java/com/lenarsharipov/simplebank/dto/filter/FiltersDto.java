package com.lenarsharipov.simplebank.dto.filter;

import lombok.Value;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Value
public class FiltersDto {

    String phone;
    String email;
    String fullName;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate birthDate;
}
