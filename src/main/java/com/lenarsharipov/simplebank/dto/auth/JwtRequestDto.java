package com.lenarsharipov.simplebank.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Login request")
public class JwtRequestDto {

    @Schema(description = "user name", example = "myUsername")
    @NotBlank(message = "Username cannot be blank and null")
    private String username;

    @Schema(description = "password", example = "P4ssword")
    @NotBlank(message = "Password cannot be blank and null")
    private String password;
}
