package com.lenarsharipov.simplebank.service;

import com.lenarsharipov.simplebank.dto.auth.JwtRequestDto;
import com.lenarsharipov.simplebank.dto.auth.JwtResponseDto;
import com.lenarsharipov.simplebank.model.User;
import com.lenarsharipov.simplebank.security.JwtTokenProvider;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    public JwtResponseDto login(JwtRequestDto dto) {
        var jwtResponse = new JwtResponseDto();
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.getUsername(), dto.getPassword()));
        User user = userService.getByUsername(dto.getUsername());
        jwtResponse.setId(user.getId());
        jwtResponse.setUsername(user.getUsername());
        jwtResponse.setAccessToken(
                jwtTokenProvider.createAccessToken(
                        user.getId(), user.getUsername()));
        return jwtResponse;
    }
}
