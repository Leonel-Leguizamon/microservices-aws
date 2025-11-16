package com.dlsoftware.msvc.products_msvc.commons.auditable;

import lombok.AllArgsConstructor;
import org.hibernate.resource.jdbc.spi.StatementInspector;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class CustomStatementInspector implements StatementInspector {

    private final AuditAwareImpl auditAware;

    @Override
    public String inspect(String sql) {
        if (sql.contains(":current_user")) {
            sql = sql.replace(":current_user", "'" + getCurrentUser() + "'");
        }
        return sql;
    }

    private String getCurrentUser() {
        return auditAware.getCurrentAuditor().orElse("?");
    }
}