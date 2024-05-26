package com.lenarsharipov.simplebank.security;

import com.lenarsharipov.simplebank.model.User;
import com.lenarsharipov.simplebank.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userService.getByUsername(username);
        return JwtEntityFactory.create(user);
    }
}
