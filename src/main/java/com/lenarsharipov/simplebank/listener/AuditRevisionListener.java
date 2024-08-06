package com.lenarsharipov.simplebank.listener;

import com.lenarsharipov.simplebank.audit.Revision;
import org.hibernate.envers.RevisionListener;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuditRevisionListener implements RevisionListener {

    @Override
    public void newRevision(Object revEntity) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication == null ? "system" : authentication.getName();
        ((Revision) revEntity).setUsername(username);
    }
}