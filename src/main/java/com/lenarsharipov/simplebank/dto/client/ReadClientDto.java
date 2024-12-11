package com.lenarsharipov.simplebank.dto.client;

import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record ReadClientDto(Long id,
                            String username,
                            String fullName,
                            LocalDate birthDate,
                            String accountNumber,
                            List<String> emails,
                            List<String> phones) {
}
