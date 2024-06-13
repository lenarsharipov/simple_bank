package com.lenarsharipov.simplebank.dto.email;

import com.lenarsharipov.simplebank.validation.UniqueEmail;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateEmailDto(
    @UniqueEmail
    @Email
    @NotBlank(message = "Email Cannot be null or blank")
    String email
) {
}
