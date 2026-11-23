package com.flagship.Auth_Service.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.Instant;


import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Data
@Table(name = "refresh_tokens")
public class RefreshToken {

    @Id
    @GeneratedValue
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false)
    private UUID userId;  // FK to User.id

    @Column(nullable = false)
    private String tokenHash;

    private Instant expiresAt;
}


