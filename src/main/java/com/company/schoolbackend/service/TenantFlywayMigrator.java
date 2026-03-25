package com.company.schoolbackend.service;

import com.company.schoolbackend.service.TenantRegistry.TenantInfo;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class TenantFlywayMigrator implements ApplicationRunner {
    private final TenantRegistry tenantRegistry;
    private final String locations;
    private final boolean baselineOnMigrate;

    public TenantFlywayMigrator(
            TenantRegistry tenantRegistry,
            @Value("${tenant.flyway.locations:classpath:db/migration}") String locations,
            @Value("${tenant.flyway.baseline-on-migrate:true}") boolean baselineOnMigrate
    ) {
        this.tenantRegistry = tenantRegistry;
        this.locations = locations;
        this.baselineOnMigrate = baselineOnMigrate;
    }

    @Override
    public void run(ApplicationArguments args) {
        for (TenantInfo tenant : tenantRegistry.listTenants()) {
            Flyway.configure()
                    .dataSource(tenant.getDbUrl(), tenant.getDbUsername(), tenant.getDbPassword())
                    .locations(locations)
                    .baselineOnMigrate(baselineOnMigrate)
                    .load()
                    .migrate();
        }
    }
}
