package me.personal.dinner_planner.controllers;

import lombok.RequiredArgsConstructor;
import me.personal.dinner_planner.dto.auth.LoginRequest;
import me.personal.dinner_planner.dto.auth.PasswordResetRequest;
import me.personal.dinner_planner.dto.auth.PasswordUpdateRequest;
import me.personal.dinner_planner.dto.auth.RegistrationRequest;
import me.personal.dinner_planner.services.EmailPasswordAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final EmailPasswordAuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody RegistrationRequest request) {
        authService.register(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest request,
                                      HttpServletResponse response) {
        String token = authService.login(request);

        Cookie cookie = new Cookie("jwt_token", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(true); // Enable in production
        cookie.setPath("/");
        cookie.setMaxAge(86400); // 24 hours

        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/password-reset/request")
    public ResponseEntity<Void> requestPasswordReset(@RequestBody PasswordResetRequest request) {
        authService.initiatePasswordReset(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/password-reset/confirm")
    public ResponseEntity<Void> resetPassword(@RequestBody PasswordUpdateRequest request) {
        authService.resetPassword(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("jwt_token", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);

        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }
}
