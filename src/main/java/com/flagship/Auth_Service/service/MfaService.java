package com.flagship.Auth_Service.service;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorConfig;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MfaService {
    private final GoogleAuthenticator gAuth;

    /*
    So setWindowSize(1) with 1, you accept:
    Previous code
    Current code
    Next code
     */

    public MfaService() {
        GoogleAuthenticatorConfig config = new GoogleAuthenticatorConfig.GoogleAuthenticatorConfigBuilder()
                .setWindowSize(1)   // ±1 time window (30s before/after)
                .build();

        this.gAuth = new GoogleAuthenticator(config);
    }

    public boolean verify(String secret, int otp) {
        return gAuth.authorize(secret, otp);
    }
}

