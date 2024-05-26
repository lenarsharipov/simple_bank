package com.lenarsharipov.simplebank.security;

import com.lenarsharipov.simplebank.properties.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final JwtProperties jwtProperties;
    private final UserDetailsService userDetailsService;

    private SecretKey key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes());
    }

    public String createAccessToken(Long userId,
                                    String username) {
        Claims claims = Jwts.claims()
                .subject(username)
                .add("id", userId)
                .build();

        Instant validity = Instant.now()
                .plus(jwtProperties.getAccess(), ChronoUnit.HOURS);

        return Jwts.builder()
                .claims(claims)
                .expiration(Date.from(validity))
                .signWith(key)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        String username = getUsername(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(
                userDetails,  "", userDetails.getAuthorities());
    }

    public boolean isValid(String token) {
        Jws<Claims> claims = Jwts
                .parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token);

        return claims.getPayload()
                .getExpiration()
                .after(new Date());
    }

    private String getUsername(String token) {
        return Jwts
                .parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
}
