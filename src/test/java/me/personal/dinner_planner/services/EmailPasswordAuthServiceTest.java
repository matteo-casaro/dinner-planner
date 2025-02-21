package me.personal.dinner_planner.services;

import me.personal.dinner_planner.configuration.JwtService;
import me.personal.dinner_planner.dto.auth.LoginRequest;
import me.personal.dinner_planner.dto.auth.PasswordResetRequest;
import me.personal.dinner_planner.dto.auth.PasswordUpdateRequest;
import me.personal.dinner_planner.dto.auth.RegistrationRequest;
import me.personal.dinner_planner.exceptions.*;
import me.personal.dinner_planner.interfaces.EmailService;
import me.personal.dinner_planner.models.User;
import me.personal.dinner_planner.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Tag("UnitTest")
public class EmailPasswordAuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private EmailPasswordAuthService authService;

    private User testUser;

    @BeforeEach
    void generateTestUser() {
        testUser = User.builder().email("john_doe@email.org").password("password").build();
    }

    @Test
    void shouldRegisterNewUser() {
        RegistrationRequest request = new RegistrationRequest("john_doe@email.org", "password");
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(request.getPassword())).thenReturn("hashedNewPassword");
        authService.register(request);
        verify(userRepository).save(argThat(user -> user.getEmail().equals(request.getEmail()) && user.getPassword().equals("hashedNewPassword")));
    }

    @Test
    void shouldThrowExceptionIfUserAlreadyExists() {
        RegistrationRequest request = new RegistrationRequest("john_doe@email.org", "password");
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(testUser));
        assertThrows(UserAlreadyExistsException.class, () -> authService.register(request));
    }

    @Test
    void shouldLoginSuccessfully() {
        LoginRequest request = new LoginRequest("john_doe@email.org", "password");
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(request.getPassword(), testUser.getPassword())).thenReturn(true);
        when(jwtService.generateToken(testUser.getId())).thenReturn("mockToken");
        String token = authService.login(request);
        assertEquals("mockToken", token);
    }

    @Test
    void shouldThrowInvalidCredentialsExceptionIfUserNotFound() {
        LoginRequest request = new LoginRequest("john_doe@email.org", "password");
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        assertThrows(InvalidCredentialsException.class, () -> authService.login(request));
    }

    @Test
    void shouldThrowInvalidCredentialsExceptionIfPasswordIsWrong() {
        LoginRequest request = new LoginRequest("john_doe@email.org", "wrongPassword");
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(request.getPassword(), testUser.getPassword())).thenReturn(false);
        assertThrows(InvalidCredentialsException.class, () -> authService.login(request));
    }

    @Test
    void shouldInitiatePasswordReset() {
        PasswordResetRequest request = new PasswordResetRequest("john_doe@email.org");
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(testUser));
        authService.initiatePasswordReset(request);
        assertNotNull(testUser.getTempToken());
        assertNotNull(testUser.getTempTokenExp());
        verify(emailService).sendPasswordResetEmail(eq(testUser.getEmail()), anyString());
        verify(userRepository).save(eq(testUser));
    }

    @Test
    void shouldThrowUserNotFoundExceptionIfEmailNotExists() {
        PasswordResetRequest request = new PasswordResetRequest("nonexistent@example.com");
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> authService.initiatePasswordReset(request));
    }

    @Test
    void shouldResetPasswordSuccessfully() {
        PasswordUpdateRequest request = new PasswordUpdateRequest("validToken", "newPassword");
        testUser.setTempToken("validToken");
        testUser.setTempTokenExp(LocalDateTime.now().plusHours(1));
        when(userRepository.findByTempToken(request.getToken())).thenReturn(Optional.of(testUser));
        when(passwordEncoder.encode(request.getNewPassword())).thenReturn("hashedNewPassword");
        authService.resetPassword(request);
        assertNull(testUser.getTempToken());
        assertNull(testUser.getTempTokenExp());
        assertEquals("hashedNewPassword", testUser.getPassword());
        verify(userRepository).save(eq(testUser));
    }

    @Test
    void shouldThrowInvalidTokenExceptionIfTokenNotFound() {
        PasswordUpdateRequest request = new PasswordUpdateRequest("invalidToken", "newPassword");
        when(userRepository.findByTempToken(request.getToken())).thenReturn(Optional.empty());
        assertThrows(InvalidTokenException.class, () -> authService.resetPassword(request));
    }

    @Test
    void shouldThrowTokenExpiredExceptionIfTokenExpired() {
        PasswordUpdateRequest request = new PasswordUpdateRequest("expiredToken", "newPassword");
        testUser.setTempToken("expiredToken");
        testUser.setTempTokenExp(LocalDateTime.now().minusHours(1));
        when(userRepository.findByTempToken(request.getToken())).thenReturn(Optional.of(testUser));
        assertThrows(TokenExpiredException.class, () -> authService.resetPassword(request));
    }
}
