package com.company.schoolbackend.config;

public class TenantContext {
    private static final ThreadLocal<String> CURRENT = new ThreadLocal<>();

    public static void setTenant(String tenantKey) {
        CURRENT.set(tenantKey);
    }

    public static String getTenant() {
        return CURRENT.get();
    }

    public static void clear() {
        CURRENT.remove();
    }
}
