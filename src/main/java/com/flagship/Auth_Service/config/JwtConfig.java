package com.flagship.Auth_Service.config;

import com.flagship.Auth_Service.entity.Role;
import com.flagship.Auth_Service.entity.User;
import io.jsonwebtoken.Jwts;
import lombok.Getter;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

@Component
//@Getter
public class JwtConfig {

    private final PrivateKey privateKey;
//    private final PublicKey publicKey;

    public JwtConfig() throws Exception {
        ClassPathResource resource = new ClassPathResource("keys/private.key");
        // Remove PEM header/footer and newlines
        String key = Files.readString(resource.getFile().toPath());
        key = key
//                .replace("-----BEGIN PRIVATE KEY-----", "")
//                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");

        byte[] decoded = Base64.getDecoder().decode(key);

        this.privateKey = KeyFactory.getInstance("RSA")
                .generatePrivate(new PKCS8EncodedKeySpec(decoded));

//        ClassPathResource resourcePublic = new ClassPathResource("keys/public.key");
//        // Remove PEM header/footer and newlines
//        String pubkey = Files.readString(resourcePublic.getFile().toPath());
//        pubkey = pubkey.replace("-----BEGIN PUBLIC KEY-----", "")
//                .replace("-----END PUBLIC KEY-----", "")
//                .replaceAll("\\s", "");
//
//        byte[] pubDecoded = Base64.getDecoder().decode(pubkey);
//
//        this.publicKey = KeyFactory.getInstance("RSA")
//                .generatePublic(new X509EncodedKeySpec(pubDecoded));
    }

    public String generateAccessToken(User user) {
        return Jwts.builder()
                .setIssuer("auth.bank.com")
                .setSubject(String.valueOf(user.getId()))
                .setAudience("payment-api")
//                .claim("scope", user.getRoles())
                .claim("scope",
                String.join(" ",
                        user.getRoles()
                                .stream()
                                .map(Role::getName)
                                .toList()
                )
        )

                .setId(UUID.randomUUID().toString())
                .setIssuedAt(new Date())
                .setExpiration(Date.from(
                        Instant.now().plusSeconds(600)))
                .signWith(privateKey)
                .compact();
    }

}
