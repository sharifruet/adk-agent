package com.i2gether.lic.config;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Map;

public class ApiKeySetter {

    public static void setApiKeyFromYaml() {
        try {
            // Read YAML directly from classpath
            InputStream inputStream = ApiKeySetter.class.getClassLoader()
                .getResourceAsStream("application.yaml");
            
            if (inputStream != null) {
                try {
                    Yaml yaml = new Yaml();
                    Map<String, Object> yamlMap = yaml.load(inputStream);
                    
                    // Navigate to the api-key property: com.i2gether.lic.agent.api-key
                    String apiKey = extractApiKey(yamlMap);
                    
                    if (apiKey != null && !apiKey.isBlank()) {
                        // Set as system properties (library checks both GOOGLE_API_KEY and GEMINI_API_KEY)
                        System.setProperty("GEMINI_API_KEY", apiKey);
                        System.setProperty("GOOGLE_API_KEY", apiKey);
                        
                        // Try to set as environment variables using reflection
                        // Note: GEMINI_API_KEY is set first since this is a Gemini API key
                        setEnvironmentVariable("GEMINI_API_KEY", apiKey);
                        setEnvironmentVariable("GOOGLE_API_KEY", apiKey);
                    }
                } finally {
                    inputStream.close();
                }
            }
        } catch (Exception e) {
            // If reading YAML fails, the ApplicationContextInitializer will try to set it
            System.err.println("Warning: Could not read API key from application.yaml: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private static String extractApiKey(Map<String, Object> yamlMap) {
        try {
            Map<String, Object> com = (Map<String, Object>) yamlMap.get("com");
            if (com != null) {
                Map<String, Object> i2gether = (Map<String, Object>) com.get("i2gether");
                if (i2gether != null) {
                    Map<String, Object> lic = (Map<String, Object>) i2gether.get("lic");
                    if (lic != null) {
                        Map<String, Object> agent = (Map<String, Object>) lic.get("agent");
                        if (agent != null) {
                            return (String) agent.get("api-key");
                        }
                    }
                }
            }
        } catch (Exception e) {
            // Ignore
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private static void setEnvironmentVariable(String key, String value) {
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

