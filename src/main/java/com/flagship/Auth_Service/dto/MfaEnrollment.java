package com.flagship.Auth_Service.dto;

public record MfaEnrollment(
        String secret,
        String qrCodeUrl
) {}
