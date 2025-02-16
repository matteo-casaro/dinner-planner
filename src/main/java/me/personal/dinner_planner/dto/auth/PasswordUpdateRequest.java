package me.personal.dinner_planner.dto.auth;

import lombok.Data;

@Data
public class PasswordUpdateRequest {
    private String token;
    private String newPassword;
}
