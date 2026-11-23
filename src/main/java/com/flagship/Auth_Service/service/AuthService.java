package com.flagship.Auth_Service.service;

import com.flagship.Auth_Service.config.JwtConfig;
import com.flagship.Auth_Service.entity.User;
import com.flagship.Auth_Service.repo.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthService {

    private final UserRepository userRepo;
    private final PasswordEncoder encoder;
    private final MfaService mfaService;
    private final JwtConfig jwtConfig;

    public User authenticate(String username, String password) {

        com.flagship.Auth_Service.entity.User user = userRepo.findByUsername(username)
                .orElseThrow();

        if (!encoder.matches(password, user.getPasswordHash())) {
            throw new RuntimeException("Invalid credentials");
        }

        return user;
    }

    public User findById(String userId) {
        Optional<User> userOptional = userRepo.findById(userId);
        return userOptional.orElse(null);
    }

    public String login(String username, String password, Integer otp) {

        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!encoder.matches(password, user.getPasswordHash())) {
            throw new RuntimeException("Invalid credentials");
        }

        if (user.isMfaEnabled()) {

            if (otp == null) {
                throw new RuntimeException("MFA code required");
            }

            boolean valid = mfaService.verify(
                    user.getMfaSecret(),
                    otp
            );

            if (!valid) {
                throw new RuntimeException("Invalid MFA code");
            }
        }

        // ✅ MFA passed → generate JWT & refresh token
        return jwtConfig.generateAccessToken(user);
    }
}

