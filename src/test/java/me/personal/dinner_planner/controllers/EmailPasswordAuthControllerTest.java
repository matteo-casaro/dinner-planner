package me.personal.dinner_planner.controllers;

import me.personal.dinner_planner.dto.auth.LoginRequest;
import me.personal.dinner_planner.dto.auth.PasswordResetRequest;
import me.personal.dinner_planner.dto.auth.PasswordUpdateRequest;
import me.personal.dinner_planner.dto.auth.RegistrationRequest;
import me.personal.dinner_planner.services.EmailPasswordAuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@Tag("UnitTest")
class EmailPasswordAuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private EmailPasswordAuthService authService;

    @InjectMocks
    private EmailPasswordAuthController emailPasswordAuthController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(emailPasswordAuthController).build();
    }

    @Test
    void register_ShouldReturnOk() throws Exception {
        doNothing().when(authService).register(any(RegistrationRequest.class));
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"test@example.com\",\"password\":\"password123\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void login_ShouldReturnOkAndSetCookie() throws Exception {
        when(authService.login(any(LoginRequest.class))).thenReturn("mock-jwt-token");
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"test@example.com\",\"password\":\"password123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jwt").value("mock-jwt-token"))
                .andExpect(cookie().exists("jwt_token"))
                .andExpect(cookie().value("jwt_token", "mock-jwt-token"));
    }

    @Test
    void requestPasswordReset_ShouldReturnOk() throws Exception {
        doNothing().when(authService).initiatePasswordReset(any(PasswordResetRequest.class));
        mockMvc.perform(post("/api/auth/password-reset/request")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"test@example.com\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void resetPassword_ShouldReturnOk() throws Exception {
        doNothing().when(authService).resetPassword(any(PasswordUpdateRequest.class));
        mockMvc.perform(post("/api/auth/password-reset/confirm")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"token\":\"reset-token\",\"newPassword\":\"new-password\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void logout_ShouldReturnOkAndClearCookie() throws Exception {
        mockMvc.perform(post("/api/auth/logout"))
                .andExpect(status().isOk())
                .andExpect(cookie().exists("jwt_token"))
                .andExpect(cookie().maxAge("jwt_token", 0));
    }
}

