package com.i2gether.lic.config;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import java.lang.reflect.Field;
import java.util.Map;

public class ApiKeyEnvironmentInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        String apiKey = environment.getProperty("com.i2gether.lic.agent.api-key");
        
        if (apiKey != null && !apiKey.isBlank()) {
            // Set as system properties (library checks both GOOGLE_API_KEY and GEMINI_API_KEY)
            // Note: GEMINI_API_KEY is set first since this is a Gemini API key
            System.setProperty("GEMINI_API_KEY", apiKey);
            System.setProperty("GOOGLE_API_KEY", apiKey);
            
            // Also try to set as environment variables
            setEnvironmentVariable("GEMINI_API_KEY", apiKey);
            setEnvironmentVariable("GOOGLE_API_KEY", apiKey);
        }
    }

    @SuppressWarnings("unchecked")
    private void setEnvironmentVariable(String key, String value) {
        try {
            // Try to get the ProcessEnvironment class
            Class<?> processEnvironmentClass = Class.forName("java.lang.ProcessEnvironment");
            
            // Try to get the theEnvironment field
            Field theEnvironmentField = processEnvironmentClass.getDeclaredField("theEnvironment");
            theEnvironmentField.setAccessible(true);
            Map<String, String> env = (Map<String, String>) theEnvironmentField.get(null);
            env.put(key, value);
            
            // Also try to set the caseInsensitiveEnvironment for Windows
            try {
                Field caseInsensitiveEnvironmentField = processEnvironmentClass.getDeclaredField("theCaseInsensitiveEnvironment");
                caseInsensitiveEnvironmentField.setAccessible(true);
                Map<String, String> ciEnv = (Map<String, String>) caseInsensitiveEnvironmentField.get(null);
                ciEnv.put(key, value);
            } catch (Exception e) {
                // Ignore if not on Windows or field doesn't exist
            }
        } catch (Exception e) {
            // Fallback: try the unmodifiable map approach
            try {
                Map<String, String> env = System.getenv();
                Class<?> cl = env.getClass();
                Field field = cl.getDeclaredField("m");
                field.setAccessible(true);
                Map<String, String> writableEnv = (Map<String, String>) field.get(env);
                writableEnv.put(key, value);
            } catch (Exception ex) {
                // If all else fails, system property should work
            }
        }
    }
}

