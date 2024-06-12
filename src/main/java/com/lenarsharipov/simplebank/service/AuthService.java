package com.lenarsharipov.simplebank.service;

import com.lenarsharipov.simplebank.dto.auth.JwtRequestDto;
import com.lenarsharipov.simplebank.dto.auth.JwtResponseDto;
import com.lenarsharipov.simplebank.model.User;
import com.lenarsharipov.simplebank.security.JwtTokenProvider;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@AllArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider provider;
    private final UserService userService;

    public JwtResponseDto login(JwtRequestDto dto) {
        var authenticationToken =
                new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword());
        authenticationManager.authenticate(authenticationToken);
        User user = userService.getByUsername(dto.getUsername());
        var jwtResponse = new JwtResponseDto();
        jwtResponse.setId(user.getId());
        jwtResponse.setUsername(user.getUsername());
        jwtResponse.setAccessToken(
                provider.createAccessToken(
                        user.getId(), user.getUsername(), Set.of(user.getRole())));
        jwtResponse.setRefreshToken(provider.createRefreshToken(user.getId(), user.getUsername()));
        return jwtResponse;
    }
}