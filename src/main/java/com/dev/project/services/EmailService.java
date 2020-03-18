package com.dev.project.services;

import org.springframework.mail.SimpleMailMessage;

import com.dev.project.domain.Pedido;

public interface EmailService {
	
	void sendOrderConfirmationEmail(Pedido obj);
	
	void sendEmail(SimpleMailMessage msg);
}