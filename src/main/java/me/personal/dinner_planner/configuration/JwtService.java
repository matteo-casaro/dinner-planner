package me.personal.dinner_planner.configuration;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JwtService {

    private SecretKey key;

    @PostConstruct
    public void init() {
        this.key = Jwts.SIG.HS256.key().build();
    }

    public String generateToken(UUID userId) {
        return Jwts.builder()
                .subject(userId.toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 20 * 60 * 1000))
                .signWith(key)
                .compact();
    }

    public UUID validateToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseEncryptedClaims(token)
                .getPayload();

        return UUID.fromString(claims.getSubject());
    }
}
