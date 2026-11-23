package com.flagship.Auth_Service.repo;

import com.flagship.Auth_Service.entity.User;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<@NonNull User,@NonNull String> {
    Optional<User> findByUsername(String username);
}
