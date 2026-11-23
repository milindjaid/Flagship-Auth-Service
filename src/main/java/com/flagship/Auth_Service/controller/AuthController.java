package com.flagship.Auth_Service.controller;

import com.flagship.Auth_Service.config.JwtConfig;
import com.flagship.Auth_Service.dto.LoginRequest;
import com.flagship.Auth_Service.dto.MfaRequest;
import com.flagship.Auth_Service.entity.User;
import com.flagship.Auth_Service.service.AuthService;
import com.flagship.Auth_Service.service.MfaService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/*
1. User logs in (username + password)
2. Server detects mfaEnabled = true
3. Server asks for OTP
4. User enters 6-digit code from Authenticator app
5. Server verifies OTP
6. JWT + Refresh Token issued
7. Gateway allows access
 */

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtConfig jwtConfig;
    private final MfaService mfaService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {

        User user = authService.authenticate(
                req.username(), req.password());

        if (user.isMfaEnabled()) {
            return ResponseEntity.status(202)
                    .body(Map.of("mfaRequired", true, "userId", user.getId()));
        }

        return issueToken(user);
    }

    @PostMapping("/mfa/verify")
    public ResponseEntity<?> verify(@RequestBody MfaRequest req) {

        User user = authService.findById(req.getUserId());

        if (!mfaService.verify(user.getMfaSecret(), Integer.parseInt(req.getOtp()))) {
            return ResponseEntity.status(401).build();
        }

        return issueToken(user);
    }

    @PostMapping("/loginWithMFA")
    public ResponseEntity<?> loginWithMFA(@RequestBody LoginRequest req) {
        String response = authService.login(req.username(), req.password(), req.otp());
        return ResponseEntity.ok(
                Map.of("accessToken",response));
    }

    private ResponseEntity<?> issueToken(User user) {
        return ResponseEntity.ok(
                Map.of("accessToken", jwtConfig.generateAccessToken(user)));
    }
}

