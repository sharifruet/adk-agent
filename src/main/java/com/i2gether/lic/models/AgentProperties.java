package com.i2gether.lic.models;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;

@ConfigurationProperties(prefix = "com.i2gether.lic.agent")
public record AgentProperties (
    String name,
    String description,
    String aiModel,
    Resource systemPrompt,
    String apiKey
){}