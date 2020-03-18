package com.dev.project.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;

import com.dev.project.domain.Pedido;

public abstract class AbstractEmailService implements EmailService{
	
	@Value("${default.sender}")
	private String sender;
	
	@Override
	public void sendOrderConfirmationEmail(Pedido obj) {
		SimpleMailMessage smm = prepareSimpleEmailMessage(obj);
		sendEmail(smm);
	}
	
	protected SimpleMailMessage prepareSimpleEmailMessage(Pedido obj) {
		SimpleMailMessage smm = new SimpleMailMessage();
		smm.setTo(obj.getCliente().getEmail());
		smm.setFrom(sender);
		smm.setSubject("Pedido confirmado! Código: "+obj.getId());
		smm.setSentDate(new Date(System.currentTimeMillis()));
		smm.setText(obj.toString());
		return smm;
	}
}