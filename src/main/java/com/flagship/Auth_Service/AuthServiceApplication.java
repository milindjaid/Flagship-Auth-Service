package com.flagship.Auth_Service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
Service role

- Authenticate users

- Enforce MFA

- Issue short-lived JWTs

- Manage refresh tokens

- Central SSO authority

 */

@SpringBootApplication
public class AuthServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthServiceApplication.class, args);
	}

}
