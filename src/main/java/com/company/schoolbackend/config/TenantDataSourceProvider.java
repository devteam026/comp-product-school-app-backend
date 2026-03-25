package com.company.schoolbackend.config;

import com.company.schoolbackend.service.TenantRegistry;
import com.company.schoolbackend.service.TenantRegistry.TenantInfo;
import com.zaxxer.hikari.HikariDataSource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TenantDataSourceProvider {
    private final TenantRegistry tenantRegistry;
    private final Map<String, DataSource> dataSources = new ConcurrentHashMap<>();
    private final int maxPoolSize;
    private final long maxLifetimeMs;
    private final long keepaliveTimeMs;
    private final long idleTimeoutMs;
    private final long connectionTimeoutMs;
    private final int minIdle;

    public TenantDataSourceProvider(
            TenantRegistry tenantRegistry,
            @Value("${tenant.datasource.max-pool-size:10}") int maxPoolSize,
            @Value("${tenant.datasource.max-lifetime-ms:300000}") long maxLifetimeMs,
            @Value("${tenant.datasource.keepalive-time-ms:120000}") long keepaliveTimeMs,
            @Value("${tenant.datasource.idle-timeout-ms:180000}") long idleTimeoutMs,
            @Value("${tenant.datasource.connection-timeout-ms:30000}") long connectionTimeoutMs,
            @Value("${tenant.datasource.min-idle:2}") int minIdle
    ) {
        this.tenantRegistry = tenantRegistry;
        this.maxPoolSize = maxPoolSize;
        this.maxLifetimeMs = maxLifetimeMs;
        this.keepaliveTimeMs = keepaliveTimeMs;
        this.idleTimeoutMs = idleTimeoutMs;
        this.connectionTimeoutMs = connectionTimeoutMs;
        this.minIdle = minIdle;
    }

    public DataSource getDataSource(String tenantKey) {
        String key = tenantKey == null ? "" : tenantKey.trim().toLowerCase();
        if (key.isBlank()) {
            throw new IllegalArgumentException("Tenant key required");
        }
        return dataSources.computeIfAbsent(key, this::createDataSource);
    }

    private DataSource createDataSource(String tenantKey) {
        TenantInfo info = tenantRegistry.getTenant(tenantKey);
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl(info.getDbUrl());
        ds.setUsername(info.getDbUsername());
        ds.setPassword(info.getDbPassword());
        ds.setMaximumPoolSize(maxPoolSize);
        ds.setMinimumIdle(minIdle);
        ds.setPoolName("tenant-" + tenantKey);
        ds.setMaxLifetime(maxLifetimeMs);
        if (keepaliveTimeMs > 0) {
            ds.setKeepaliveTime(keepaliveTimeMs);
        }
        if (idleTimeoutMs > 0) {
            ds.setIdleTimeout(idleTimeoutMs);
        }
        ds.setConnectionTimeout(connectionTimeoutMs);
        ds.setValidationTimeout(5000);
        ds.setConnectionTestQuery("SELECT 1");
        return ds;
    }
}
