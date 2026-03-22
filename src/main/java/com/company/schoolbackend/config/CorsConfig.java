package com.company.schoolbackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {
    private final String[] allowedOrigins;

    public CorsConfig(@Value("${school.cors.allowed-origins:http://localhost:3000,http://127.0.0.1:3000}") String allowedOrigins) {
        this.allowedOrigins = allowedOrigins.split(",");
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                var mapping = registry.addMapping("/api/**");
                boolean allowAll = false;
                for (String origin : allowedOrigins) {
                    if ("*".equals(origin.trim())) {
                        allowAll = true;
                        break;
                    }
                }
                if (allowAll) {
                    mapping.allowedOriginPatterns("*");
                } else {
                    mapping.allowedOrigins(allowedOrigins);
                }
                mapping
                        .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }
}
