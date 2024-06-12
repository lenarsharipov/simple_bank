package com.lenarsharipov.simplebank.dto.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtResponseDto {

    private Long id;
    private String username;
    private String accessToken;
    private String refreshToken;
}
