package com.i2gether.lic.config;

import com.google.adk.agents.BaseAgent;
import com.google.adk.agents.LlmAgent;
import com.google.adk.tools.FunctionTool;
import com.i2gether.lic.models.AgentProperties;
import com.i2gether.lic.tool.AuthorFetcher;

import java.io.IOException;
import java.nio.charset.Charset;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(AgentProperties.class)
public class AgentConfiguration {

    @Bean
    BaseAgent baseAgent(AgentProperties agentProperties) throws IOException {
        // API key is set by ApiKeyEnvironmentInitializer before beans are created
        return LlmAgent
            .builder()
            .name(agentProperties.name())
            .description(agentProperties.description())
            .model(agentProperties.aiModel())
            .instruction(agentProperties.systemPrompt().getContentAsString(Charset.defaultCharset()))
            .tools(
                FunctionTool.create(AuthorFetcher.class, "fetch")
            )
            .build();
    }
}

