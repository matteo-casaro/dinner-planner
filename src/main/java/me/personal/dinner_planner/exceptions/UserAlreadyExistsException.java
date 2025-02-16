package me.personal.dinner_planner.exceptions;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException() {
        super("User not found");
    }
}
