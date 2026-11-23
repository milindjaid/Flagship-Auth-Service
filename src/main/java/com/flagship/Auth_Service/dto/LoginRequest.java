package com.flagship.Auth_Service.dto;

import lombok.Data;

public record LoginRequest(
     String username,
     String password,
     Integer otp
){}
