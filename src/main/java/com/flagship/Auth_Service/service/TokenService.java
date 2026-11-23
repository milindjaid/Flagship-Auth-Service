package com.flagship.Auth_Service.service;

import com.flagship.Auth_Service.entity.RefreshToken;
import com.flagship.Auth_Service.repo.RefreshTokenRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@AllArgsConstructor
public class TokenService {

    private final RefreshTokenRepository repo;

    public String createRefreshToken(UUID userId) {

        String rawToken = UUID.randomUUID().toString();
        String hash = DigestUtils.sha256Hex(rawToken);

        RefreshToken token = new RefreshToken();
        token.setId(UUID.randomUUID());
        token.setUserId(userId);
        token.setTokenHash(hash);
        token.setExpiresAt(Instant.now().plus(1, ChronoUnit.DAYS));

        repo.save(token);
        return rawToken;
    }
}

