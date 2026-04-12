package com.company.schoolbackend.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class TenantContextFilter extends OncePerRequestFilter {
    private final String defaultTenant;

    public TenantContextFilter(@Value("${tenant.default-key:default}") String defaultTenant) {
        this.defaultTenant = defaultTenant;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            String tenantKey = resolveTenant(request);
            TenantContext.setTenant(tenantKey);
            filterChain.doFilter(request, response);
        } finally {
            TenantContext.clear();
        }
    }

    private String resolveTenant(HttpServletRequest request) {
        String host = request.getServerName();
        if (host == null || host.isBlank()) {
            return defaultTenant;
        }
        String lower = host.toLowerCase();
        if ("localhost".equals(lower) || isIpAddress(lower)) {
            return defaultTenant;
        }
        if (lower.endsWith(".localhost")) {
            return lower.substring(0, lower.indexOf(".localhost"));
        }
        String[] parts = lower.split("\\.");
        if (parts.length < 3) {
            return defaultTenant;
        }
        return parts[0];
    }

    private boolean isIpAddress(String value) {
        return value.matches("\\d+\\.\\d+\\.\\d+\\.\\d+");
    }
}
