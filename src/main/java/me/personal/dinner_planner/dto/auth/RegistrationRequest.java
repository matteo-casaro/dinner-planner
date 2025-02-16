package me.personal.dinner_planner.dto.auth;

import lombok.Data;

@Data
public class RegistrationRequest {
    private String email;
    private String password;
}
