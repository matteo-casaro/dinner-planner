package me.personal.dinner_planner.interfaces;

public interface EmailService {

    void sendPasswordResetEmail(String email, String token);

}
