package com.company.schoolbackend.config;

import javax.sql.DataSource;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class TenantRoutingDataSource extends AbstractRoutingDataSource {
    private final TenantDataSourceProvider provider;
    private final String defaultTenant;

    public TenantRoutingDataSource(TenantDataSourceProvider provider, String defaultTenant) {
        this.provider = provider;
        this.defaultTenant = defaultTenant;
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return TenantContext.getTenant();
    }

    @Override
    protected DataSource determineTargetDataSource() {
        String key = (String) determineCurrentLookupKey();
        if (key == null || key.isBlank()) {
            key = defaultTenant;
        }
        return provider.getDataSource(key);
    }
}
