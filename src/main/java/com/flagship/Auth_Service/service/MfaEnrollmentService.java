package com.flagship.Auth_Service.service;

import com.flagship.Auth_Service.dto.MfaEnrollment;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MfaEnrollmentService {

    private final GoogleAuthenticator gAuth;

//    public MfaEnrollmentService() {
//        this.gAuth = new GoogleAuthenticator();
//    }

    public MfaEnrollment enroll(String username) {

        GoogleAuthenticatorKey key = gAuth.createCredentials();

        String secret = key.getKey();

        // Store SECRET ENCRYPTED in DB
//         user.setMfaSecret(secret);

        String qrUrl = GoogleAuthenticatorQRGenerator
                .getOtpAuthURL(
                        "BankApp",
                        username,
                        key
                );

        return new MfaEnrollment(secret, qrUrl);
    }
}

