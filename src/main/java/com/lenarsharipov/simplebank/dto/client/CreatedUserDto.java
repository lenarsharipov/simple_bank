package com.lenarsharipov.simplebank.dto.client;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class CreatedUserDto {

    private String username;

    private String fullName;

    private LocalDate birthDate;

    private String accountNo;

    private String email;

    private String phone;
}
