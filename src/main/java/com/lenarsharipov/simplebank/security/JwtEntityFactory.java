package com.lenarsharipov.simplebank.security;

import com.lenarsharipov.simplebank.model.Role;
import com.lenarsharipov.simplebank.model.User;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;

@UtilityClass
public class JwtEntityFactory {

    public static JwtEntity create(User user) {
        return JwtEntity.builder()
                .id(user.getId())
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(toGrantedAuthorities(List.of(Role.ROLE_CLIENT)))
                .build();
    }

    private static Collection<? extends GrantedAuthority> toGrantedAuthorities(List<Role> roles) {
        return roles.stream()
                .map(Enum::name)
                .map(SimpleGrantedAuthority::new)
                .collect(toList());
    }
}