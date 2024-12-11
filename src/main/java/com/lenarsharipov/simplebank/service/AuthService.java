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
                new UsernamePasswordAuthenticationToken(dto.username(), dto.password());
        authenticationManager.authenticate(authenticationToken);
        User user = userService.getByUsername(dto.username());
        return JwtResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .accessToken(provider.createAccessToken(
                        user.getId(), user.getUsername(), Set.of(user.getRole())))
                .refreshToken(provider.createRefreshToken(
                        user.getId(), user.getUsername()))
                .build();
    }

    public JwtResponseDto refresh(String refreshToken) {
        return provider.refreshUserTokens(refreshToken);
    }
}