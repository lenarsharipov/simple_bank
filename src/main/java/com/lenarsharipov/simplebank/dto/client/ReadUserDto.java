package com.lenarsharipov.simplebank.dto.client;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class ReadUserDto {

    private Long id;

    private String username;

    private String fullName;

    private LocalDate birthDate;

    private String accountNumber;

    private List<String> emails;

    private List<String> phones;
}
