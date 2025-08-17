package com.example.meetingnotes.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired(required = false)
    private JavaMailSender mailSender;

    public String send(String to, String subject, String body) {
        if (mailSender == null) {
            return "Email not configured. Set SMTP in application.properties.";
        }
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(to);
            msg.setSubject(subject);
            msg.setText(body);
            mailSender.send(msg);
            return "Email sent to " + to;
        } catch (Exception e) {
            return "Email failed: " + e.getMessage();
        }
    }
}
