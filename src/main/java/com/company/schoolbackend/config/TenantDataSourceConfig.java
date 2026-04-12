package com.company.schoolbackend.config;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class TenantDataSourceConfig {

    @Bean(name = "masterDataSource")
    public DataSource masterDataSource(
            @Value("${master.datasource.url}") String url,
            @Value("${master.datasource.username}") String username,
            @Value("${master.datasource.password}") String password,
            @Value("${master.datasource.driver-class-name:com.mysql.cj.jdbc.Driver}") String driver
    ) {
        com.zaxxer.hikari.HikariDataSource ds = new com.zaxxer.hikari.HikariDataSource();
        ds.setJdbcUrl(url);
        ds.setUsername(username);
        ds.setPassword(password);
        ds.setDriverClassName(driver);
        ds.setPoolName("master-db");
        return ds;
    }

    @Bean
    public JdbcTemplate masterJdbcTemplate(@Qualifier("masterDataSource") DataSource masterDataSource) {
        return new JdbcTemplate(masterDataSource);
    }

    @Bean
    @Primary
    public DataSource dataSource(
            TenantDataSourceProvider provider,
            @Value("${tenant.default-key:default}") String defaultTenant
    ) {
        TenantRoutingDataSource routing = new TenantRoutingDataSource(provider, defaultTenant);
        routing.setDefaultTargetDataSource(provider.getDataSource(defaultTenant));
        routing.setTargetDataSources(new java.util.HashMap<>());
        routing.afterPropertiesSet();
        return routing;
    }

    @Bean
    public FilterRegistrationBean<TenantContextFilter> tenantFilterRegistration(TenantContextFilter filter) {
        FilterRegistrationBean<TenantContextFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(filter);
        registration.setOrder(1);
        return registration;
    }
}
