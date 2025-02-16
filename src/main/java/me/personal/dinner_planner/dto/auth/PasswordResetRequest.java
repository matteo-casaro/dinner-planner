package me.personal.dinner_planner.dto.auth;

import lombok.Data;

@Data
public class PasswordResetRequest {
    private String email;
}
