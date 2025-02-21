package me.personal.dinner_planner.services.interfaces;

import me.personal.dinner_planner.dto.auth.LoginRequest;
import me.personal.dinner_planner.dto.auth.RegistrationRequest;
import org.springframework.transaction.annotation.Transactional;

public interface AuthService {

    @Transactional
    void register(RegistrationRequest request);

    @Transactional
    String login(LoginRequest request);
}
