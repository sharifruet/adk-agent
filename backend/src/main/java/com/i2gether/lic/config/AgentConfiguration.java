package com.i2gether.lic.config;

import com.google.adk.agents.BaseAgent;
import com.google.adk.agents.LlmAgent;
import com.google.adk.tools.FunctionTool;
import com.i2gether.lic.models.AgentProperties;
import com.i2gether.lic.services.ProductService;
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
    BaseAgent baseAgent(AgentProperties agentProperties, ProductService productService) throws IOException {
        // Get system prompt
        String systemPrompt = agentProperties.systemPrompt().getContentAsString(Charset.defaultCharset());
        
        // Append product knowledge base to system prompt
        try {
            String productKnowledge = productService.getAllProductKnowledgeBase();
            String enhancedPrompt = systemPrompt + "\n\n## Product Knowledge Base\n\n" + productKnowledge;
            
            // API key is set by ApiKeyEnvironmentInitializer before beans are created
            return LlmAgent
                .builder()
                .name(agentProperties.name())
                .description(agentProperties.description())
                .model(agentProperties.aiModel())
                .instruction(enhancedPrompt)
                .tools(
                    FunctionTool.create(AuthorFetcher.class, "fetch")
                )
                .build();
        } catch (Exception e) {
            // If product knowledge base fails to load, use base prompt only
            System.err.println("Warning: Could not load product knowledge base: " + e.getMessage());
            return LlmAgent
                .builder()
                .name(agentProperties.name())
                .description(agentProperties.description())
                .model(agentProperties.aiModel())
                .instruction(systemPrompt)
                .tools(
                    FunctionTool.create(AuthorFetcher.class, "fetch")
                )
                .build();
        }
    }
}

