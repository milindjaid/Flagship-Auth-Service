package com.flagship.Auth_Service.repo;

import com.flagship.Auth_Service.entity.RefreshToken;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<@NonNull RefreshToken,@NonNull String> {
}
