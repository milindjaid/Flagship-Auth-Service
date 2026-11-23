package com.flagship.Auth_Service.dto;

public record LoginResponse(
        String accessToken,
        boolean mfaRequired,
        String userId
) {}
