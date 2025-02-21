package me.personal.dinner_planner.services;

import lombok.RequiredArgsConstructor;
import me.personal.dinner_planner.configuration.JwtService;
import me.personal.dinner_planner.dto.auth.LoginRequest;
import me.personal.dinner_planner.dto.auth.PasswordResetRequest;
import me.personal.dinner_planner.dto.auth.PasswordUpdateRequest;
import me.personal.dinner_planner.dto.auth.RegistrationRequest;
import me.personal.dinner_planner.exceptions.*;
import me.personal.dinner_planner.interfaces.AuthService;
import me.personal.dinner_planner.interfaces.EmailService;
import me.personal.dinner_planner.models.User;
import me.personal.dinner_planner.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailPasswordAuthService implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final EmailService emailService;

    @Transactional
    @Override
    public void register(RegistrationRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException();
        }
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        userRepository.save(user);
    }

    @Transactional
    @Override
    public String login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException();
        }

        return jwtService.generateToken(user.getId());
    }

    @Transactional
    public void initiatePasswordReset(PasswordResetRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(UserNotFoundException::new);

        String token = UUID.randomUUID().toString();
        user.setTempToken(token);
        user.setTempTokenExp(LocalDateTime.now().plusHours(24));
        userRepository.save(user);

        emailService.sendPasswordResetEmail(user.getEmail(), token);
    }

    @Transactional
    public void resetPassword(PasswordUpdateRequest request) {
        User user = userRepository.findByTempToken(request.getToken())
                .orElseThrow(InvalidTokenException::new);

        if (user.getTempTokenExp().isBefore(LocalDateTime.now())) {
            throw new TokenExpiredException();
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setTempToken(null);
        user.setTempTokenExp(null);
        userRepository.save(user);
    }
}
