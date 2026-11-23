package com.flagship.Auth_Service.repo;

import com.flagship.Auth_Service.entity.Role;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<@NonNull Role,@NonNull Long> {
    Optional<Role> findByName(String admin);
}
