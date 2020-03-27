package com.dev.project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.dev.project.services.EmailService;
import com.dev.project.services.SmtpEmailService;

@Configuration
@Profile("prod")
public class ProdConfig {
	
	@Bean
	public EmailService emailService() {
		return new SmtpEmailService();
	}
}