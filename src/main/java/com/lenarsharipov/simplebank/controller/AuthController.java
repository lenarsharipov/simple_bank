package com.lenarsharipov.simplebank.controller;

import com.lenarsharipov.simplebank.dto.auth.JwtRequestDto;
import com.lenarsharipov.simplebank.dto.auth.JwtResponseDto;
import com.lenarsharipov.simplebank.dto.client.CreateClientDto;
import com.lenarsharipov.simplebank.dto.client.CreatedUserDto;
import com.lenarsharipov.simplebank.service.AuthService;
import com.lenarsharipov.simplebank.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
@Tag(name = "Auth Controller", description = "Auth API")
public class AuthController {

    private final AuthService authService;
    private final ClientService clientService;

    @PostMapping("/register")
    @ResponseStatus(CREATED)
    @Operation(summary = "Register new client with unique username, email, phone")
    public CreatedUserDto register(@Valid @RequestBody CreateClientDto dto) {
        return clientService.create(dto);
    }

    @PostMapping("/login")
    @Operation(summary = "Log in by username and password")
    public JwtResponseDto login(@Valid @RequestBody JwtRequestDto dto) {
        return authService.login(dto);
    }
}