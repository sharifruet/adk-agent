package com.i2gether.lic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.i2gether.lic.config.ApiKeyEnvironmentInitializer;
import com.i2gether.lic.config.ApiKeySetter;

@SpringBootApplication
public class LicAgentApplication {

	public static void main(String[] args) {
		// Set API key from YAML before Spring Boot starts
		ApiKeySetter.setApiKeyFromYaml();
		
		SpringApplication app = new SpringApplication(LicAgentApplication.class);
		app.addInitializers(new ApiKeyEnvironmentInitializer());
		app.run(args);
	}

}
