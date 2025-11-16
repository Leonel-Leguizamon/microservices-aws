package com.dlsoftware.msvc.products_msvc.commons.auditable;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("auditAwareImpl")
public class AuditAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.ofNullable(
//                SecurityContextHolder.getContext().getAuthentication().getName()
                "name"
        );
    }
}