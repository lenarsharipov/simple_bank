package com.lenarsharipov.simplebank.security.expression;

import com.lenarsharipov.simplebank.security.JwtEntity;
import com.lenarsharipov.simplebank.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service("customSecurityExpression")
@AllArgsConstructor
public class CustomSecurityExpression {

    private final UserService userService;

    public boolean canAccessUser(Long userId) {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        JwtEntity user = (JwtEntity) authentication.getPrincipal();
        Long inContextUserId = user.getId();
        return Objects.equals(userId, inContextUserId);
    }

    public boolean canAccessEmail(Long userId, Long emailId) {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        JwtEntity user = (JwtEntity) authentication.getPrincipal();
        Long inContextUserId = user.getId();
        return Objects.equals(userId, inContextUserId)
               && userService.isEmailOwner(userId, emailId);
    }

    public boolean canAccessPhone(Long userId, Long phoneId) {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        JwtEntity user = (JwtEntity) authentication.getPrincipal();
        Long inContextUserId = user.getId();
        return Objects.equals(userId, inContextUserId)
               && userService.isPhoneOwner(userId, phoneId);
    }
}