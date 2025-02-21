package me.personal.dinner_planner.services;

import me.personal.dinner_planner.services.interfaces.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@SuppressWarnings("unused")
public class SmtpEmailService implements EmailService {

    @Value("${fe.host}")
    private String feHost;

    private final JavaMailSender emailSender;

    public SmtpEmailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    @Override
    public void sendPasswordResetEmail(String email, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@planner.com");
        message.setTo(email);
        message.setSubject("Password reset request");
        message.setText("To reset your password, click the following link: " + feHost + "/reset-password?token=" + token);
        emailSender.send(message);
    }
}
