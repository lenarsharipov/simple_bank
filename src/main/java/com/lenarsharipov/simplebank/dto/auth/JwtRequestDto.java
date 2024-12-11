package com.lenarsharipov.simplebank.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Login request")
public record JwtRequestDto(

    @Schema(description = "user name", example = "myUsername")
    @NotBlank(message = "Username cannot be blank and null")
    String username,


    @Schema(description = "password", example = "P4ssword")
    @NotBlank(message = "Password cannot be blank and null")
    String password) {
}
