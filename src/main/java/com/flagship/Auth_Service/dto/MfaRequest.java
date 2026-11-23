package com.flagship.Auth_Service.dto;

import lombok.Data;

@Data
public class MfaRequest {
    private String userId;
    private String otp;
}
