package com.veterinaryclinic.security;

import com.veterinaryclinic.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service 
public class JwtService {
    private static final String SECRET_KEY = "VeterinaryClinicSecretKeyForJwtAuthentication2026";
    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 24;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(User user) {
        return Jwts.builder().subject(user.getEmail())
                             .claim("role", user.getRole().name())
                             .issuedAt(new Date())
                             .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                             .signWith(getSigningKey())
                             .compact();
    }
}
