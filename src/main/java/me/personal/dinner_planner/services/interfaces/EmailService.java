package me.personal.dinner_planner.services.interfaces;

public interface EmailService {

    void sendPasswordResetEmail(String email, String token);

}
