package com.company.schoolbackend.service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class TenantRegistry {
    private final JdbcTemplate jdbcTemplate;
    private final long cacheTtlMs;
    private final Map<String, CachedTenant> cache = new ConcurrentHashMap<>();

    public TenantRegistry(@Qualifier("masterJdbcTemplate") JdbcTemplate jdbcTemplate,
                          @Value("${tenant.cache-ttl-seconds:300}") long ttlSeconds) {
        this.jdbcTemplate = jdbcTemplate;
        this.cacheTtlMs = Math.max(5, ttlSeconds) * 1000L;
    }

    public TenantInfo getTenant(String tenantKey) {
        if (tenantKey == null || tenantKey.isBlank()) {
            throw new IllegalArgumentException("Tenant key required");
        }
        String key = tenantKey.trim().toLowerCase();
        CachedTenant cached = cache.get(key);
        if (cached != null && !cached.isExpired()) {
            return cached.info;
        }
        TenantInfo info = loadTenant(key);
        cache.put(key, new CachedTenant(info, Instant.now().toEpochMilli() + cacheTtlMs));
        return info;
    }

    public List<TenantInfo> listTenants() {
        return jdbcTemplate.query(
                "SELECT tenant_key, db_url, db_username, db_password, active FROM tenants WHERE active = 1",
                (rs, rowNum) -> new TenantInfo(
                        rs.getString("tenant_key"),
                        rs.getString("db_url"),
                        rs.getString("db_username"),
                        rs.getString("db_password"),
                        rs.getBoolean("active")
                )
        );
    }

    private TenantInfo loadTenant(String tenantKey) {
        List<TenantInfo> results = jdbcTemplate.query(
                "SELECT tenant_key, db_url, db_username, db_password, active FROM tenants WHERE tenant_key = ? AND active = 1",
                (rs, rowNum) -> new TenantInfo(
                        rs.getString("tenant_key"),
                        rs.getString("db_url"),
                        rs.getString("db_username"),
                        rs.getString("db_password"),
                        rs.getBoolean("active")
                ),
                tenantKey
        );
        if (results.isEmpty()) {
            throw new IllegalArgumentException("Tenant not found: " + tenantKey);
        }
        return results.get(0);
    }

    public static class TenantInfo {
        private final String tenantKey;
        private final String dbUrl;
        private final String dbUsername;
        private final String dbPassword;
        private final boolean active;

        public TenantInfo(String tenantKey, String dbUrl, String dbUsername, String dbPassword, boolean active) {
            this.tenantKey = tenantKey;
            this.dbUrl = dbUrl;
            this.dbUsername = dbUsername;
            this.dbPassword = dbPassword;
            this.active = active;
        }

        public String getTenantKey() { return tenantKey; }
        public String getDbUrl() { return dbUrl; }
        public String getDbUsername() { return dbUsername; }
        public String getDbPassword() { return dbPassword; }
        public boolean isActive() { return active; }
    }

    private static class CachedTenant {
        private final TenantInfo info;
        private final long expiresAt;

        private CachedTenant(TenantInfo info, long expiresAt) {
            this.info = info;
            this.expiresAt = expiresAt;
        }

        private boolean isExpired() {
            return System.currentTimeMillis() > expiresAt;
        }
    }
}
