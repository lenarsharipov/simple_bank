package com.lenarsharipov.simplebank.dto.auth;

import lombok.Builder;

@Builder
public record JwtResponseDto(Long id,
                             String username,
                             String accessToken,
                             String refreshToken) {
}
