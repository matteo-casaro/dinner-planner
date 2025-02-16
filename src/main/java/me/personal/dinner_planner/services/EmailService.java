package me.personal.dinner_planner.services;

public interface EmailService {

    void sendPasswordResetEmail(String email, String token);

}
