package com.lenarsharipov.simplebank.controller;

import com.lenarsharipov.simplebank.dto.auth.JwtRequestDto;
import com.lenarsharipov.simplebank.dto.auth.JwtResponseDto;
import com.lenarsharipov.simplebank.dto.user.CreateUserDto;
import com.lenarsharipov.simplebank.dto.user.CreatedUserDto;
import com.lenarsharipov.simplebank.service.AuthService;
import com.lenarsharipov.simplebank.service.UserService;
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
    private final UserService userService;

    @PostMapping("/register")
    @ResponseStatus(CREATED)
    @Operation(summary = "Register new user with unique username, email, phone")
    public CreatedUserDto register(@Valid @RequestBody CreateUserDto dto) {
        return userService.create(dto);
    }

    @PostMapping("/login")
    @Operation(summary = "Log in by username and password")
    public JwtResponseDto login(@Valid @RequestBody JwtRequestDto dto) {
        return authService.login(dto);
    }
}