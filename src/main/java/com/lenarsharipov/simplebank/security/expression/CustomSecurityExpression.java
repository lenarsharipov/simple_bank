package com.lenarsharipov.simplebank.security.expression;

import com.lenarsharipov.simplebank.model.Role;
import com.lenarsharipov.simplebank.security.JwtEntity;
import com.lenarsharipov.simplebank.service.ClientService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service("customSecurityExpression")
@AllArgsConstructor
public class CustomSecurityExpression {

    private final ClientService clientService;

    public boolean isAdmin() {
        JwtEntity user = getJwtEntity();
        return user.getAuthorities()
                .contains(new SimpleGrantedAuthority(Role.ROLE_ADMIN.name()));
    }

    public boolean canAccessUser(Long clientId) {
        JwtEntity user = getJwtEntity();
        return isAdmin() || clientService.isUserOwner(clientId, user.getId());
    }

    private static JwtEntity getJwtEntity() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        return (JwtEntity) authentication.getPrincipal();
    }

    public boolean canAccessEmail(Long userId, Long emailId) {
        return canAccessUser(userId) && clientService.isEmailOwner(userId, emailId);
    }

    public boolean canAccessPhone(Long userId, Long phoneId) {
        return canAccessUser(userId) && clientService.isPhoneOwner(userId, phoneId);
    }
}