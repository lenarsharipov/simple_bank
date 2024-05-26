package com.lenarsharipov.simplebank.dto.user;

import com.lenarsharipov.simplebank.validation.LegalAge;
import com.lenarsharipov.simplebank.validation.UniqueEmail;
import com.lenarsharipov.simplebank.validation.UniqueUsername;
import com.lenarsharipov.simplebank.validation.UniquePhone;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDate;

@Value
@Builder
@Schema(description = "User Dto")
public class CreateUserDto {

    @Schema(description = "username", example = "myUsername")
    @UniqueUsername
    @Size(min = 4,
            max = 128,
            message = "Username length must be between 4 and 128 characters")
    @NotBlank(message = "Username cannot be empty or null")
    String username;

    @Schema(description = "password", example = "P4ssword")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$",
            message = "Must have uppercase, lowercase letters and number")
    @Size(min = 8,
            max = 128,
            message = "Password length must be between 8 and 128 characters")
    @NotBlank(message = "Password cannot be empty or null")
    String password;

    @Schema(description = "Full name", example = "Ivan")
    @Size(min = 3,
            max = 255,
            message = "Full name length must be between 3 and 128 characters")
    @NotBlank(message = "Full name cannot be empty or null")
    String fullName;

    @Schema(description = "Birth date", example = "2000-01-01")
    @LegalAge
    @NotNull(message = "Birth date cannot be null")
    @Past(message = "Birth date cannot be now or future")
    LocalDate birthDate;

    @Schema(description = "Email", example = "ivanov@mail.com")
    @UniqueEmail
    @Email
    @NotBlank(message = "Email Cannot be null or blank")
    String email;

    @Schema(description = "Phone", example = "7777777")
    @UniquePhone
    @Digits(integer = 10, fraction = 0)
    @NotBlank(message = "Phone cannot be null or blank")
    String phone;

    @Schema(description = "Initial account balance", example = "10 000")
    @Digits(integer = 5, fraction = 2)
    @Positive(message = "Initial balance must be > 0")
    @NotNull(message = "Cannot be null")
    BigDecimal initialBalance;
}