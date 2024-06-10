package com.lenarsharipov.simplebank.security.expression;

import com.lenarsharipov.simplebank.model.Role;
import com.lenarsharipov.simplebank.security.JwtEntity;
import com.lenarsharipov.simplebank.service.ClientService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service("customSecurityExpression")
@AllArgsConstructor
public class CustomSecurityExpression {

    private final ClientService clientService;

    public boolean canAccessUser(Long clientId) {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        JwtEntity user = (JwtEntity) authentication.getPrincipal();

        Long inContextUserId = user.getId();
        return user.getAuthorities().contains(Role.ROLE_ADMIN)
                    || clientService.isUser(clientId, inContextUserId);
    }

    public boolean canAccessEmail(Long userId,
                                  Long emailId) {
        return canAccessUser(userId)
               && clientService.isEmailOwner(userId, emailId);
    }

    public boolean canAccessPhone(Long userId,
                                  Long phoneId) {
        return canAccessUser(userId)
               && clientService.isPhoneOwner(userId, phoneId);
    }
}