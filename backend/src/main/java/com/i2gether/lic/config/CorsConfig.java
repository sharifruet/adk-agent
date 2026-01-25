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
        
        // Allow frontend origins
        config.setAllowCredentials(true);
        config.addAllowedOrigin("https://lic-agent.i2gether.com");
        config.addAllowedOrigin("http://lic-agent.i2gether.com");
        config.addAllowedOrigin("https://agent.i2gether.com");
        config.addAllowedOrigin("http://agent.i2gether.com"); // Allow both http and https
        config.addAllowedHeader("*");
        config.addAllowedMethod("GET");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("DELETE");
        config.addAllowedMethod("OPTIONS");
        
        // Expose headers that frontend might need
        config.addExposedHeader("Content-Type");
        config.addExposedHeader("Authorization");
        
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}

