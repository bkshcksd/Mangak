package io.virinchi.mangak.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;


    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendWelcomeEmail(
            String toEmail,
            String username
    ) {

        SimpleMailMessage message =
                new SimpleMailMessage();


        message.setFrom(
                fromEmail
        );


        message.setTo(
                toEmail
        );


        message.setSubject(
                "Welcome to MANGAK"
        );


        message.setText(
                "Hello " + username + ",\n\n" +
                        "Thanks for joining MANGAK.\n\n" +
                        "Your account has been created successfully.\n\n" +
                        "MANGAK"
        );


        mailSender.send(
                message
        );
    }
}