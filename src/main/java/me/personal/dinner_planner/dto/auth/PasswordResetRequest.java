package me.personal.dinner_planner.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PasswordResetRequest {
    private String email;
}
