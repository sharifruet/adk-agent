package com.i2gether.lic.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        
        // Allow specific origins: localhost and i2gether.com domains
        config.setAllowCredentials(false);
        config.addAllowedOriginPattern("http://localhost:*");
        config.addAllowedOriginPattern("https://*.i2gether.com");
        config.addAllowedOriginPattern("http://*.i2gether.com");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        
        // Expose headers that frontend might need
        config.addExposedHeader("Content-Type");
        config.addExposedHeader("Authorization");
        
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}

