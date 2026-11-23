package com.flagship.Auth_Service.config;

import com.flagship.Auth_Service.dto.MfaEnrollment;
import com.flagship.Auth_Service.entity.Role;
import com.flagship.Auth_Service.entity.User;
import com.flagship.Auth_Service.repo.RoleRepository;
import com.flagship.Auth_Service.repo.UserRepository;
import com.flagship.Auth_Service.service.MfaEnrollmentService;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@AllArgsConstructor
public class UserSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final MfaEnrollmentService mfaEnrollmentService;

    @Override
    public void run(String... args) {
        // Seed roles if not already present
        Role adminRole = roleRepository.findByName("ADMIN")
                .orElseGet(() -> roleRepository.save(new Role(null, "ADMIN", "System administrator")));
        Role userRole = roleRepository.findByName("USER")
                .orElseGet(() -> roleRepository.save(new Role(null, "USER", "Bank user")));
        Role supportRole = roleRepository.findByName("SUPPORT")
                .orElseGet(() -> roleRepository.save(new Role(null, "SUPPORT", "Support staff")));

        // List of test users
        List<TestUser> testUsers = List.of(
                new TestUser("john.doe", "Password123!", true, Set.of(userRole)),
                new TestUser("jane.smith", "SecurePass1!", false, Set.of(userRole)),
                new TestUser("alice.jones", "AdminPass1!", true, Set.of(adminRole, userRole)),
                new TestUser("bob.brown", "UserPass123", true, Set.of(userRole)),
                new TestUser("carol.white", "Support!234", false, Set.of(supportRole)),
                new TestUser("eve.black", "Bank123!", true, Set.of(userRole)),
                new TestUser("milind.jaid", "MJ123!", true, Set.of(userRole))
        );

        for (TestUser t : testUsers) {
            if (userRepository.findByUsername(t.username).isPresent()) {
                continue; // skip existing users
            }

            User user = new User();
            user.setUsername(t.username);
            user.setPasswordHash(passwordEncoder.encode(t.password));
            user.setMfaEnabled(t.mfaEnabled);
            user.setRoles(t.roles);
            userRepository.save(user);

            // Generate MFA secret if MFA is enabled
            if (t.mfaEnabled) {
                MfaEnrollment enrollment = mfaEnrollmentService.enroll(user.getUsername());
                user.setMfaSecret(enrollment.secret());
                userRepository.save(user);
                System.out.println("MFA QR URL for " + user.getUsername() + ": " + enrollment.qrCodeUrl());
            } else {
                System.out.println("Created user without MFA: " + user.getUsername());
            }
        }
    }

    private static class TestUser {
        String username;
        String password;
        boolean mfaEnabled;
        Set<Role> roles;

        public TestUser(String username, String password, boolean mfaEnabled, Set<Role> roles) {
            this.username = username;
            this.password = password;
            this.mfaEnabled = mfaEnabled;
            this.roles = roles;
        }
    }
}
