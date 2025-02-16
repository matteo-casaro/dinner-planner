package me.personal.dinner_planner.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super("User with this email already exists");
    }
}
