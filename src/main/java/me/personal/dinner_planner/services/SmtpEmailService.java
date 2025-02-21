package me.personal.dinner_planner.services;

import me.personal.dinner_planner.interfaces.EmailService;
import org.springframework.stereotype.Service;

@Service
@SuppressWarnings("unused")
public class SmtpEmailService implements EmailService {

    @Override
    public void sendPasswordResetEmail(String email, String token) {
        System.out.println("Sending token" + token + " to email " + email);
    }
}
