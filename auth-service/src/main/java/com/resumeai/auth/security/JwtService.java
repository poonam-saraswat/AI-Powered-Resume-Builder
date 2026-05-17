package com.resumeai.auth.security;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;

@Service
public class JwtService {
    @Value("${app.jwt.secret}") private String secret;
    @Value("${app.jwt.access-ttl-seconds:3600}") private long accessTtl;
    @Value("${app.jwt.refresh-ttl-seconds:1209600}") private long refreshTtl;

    private SecretKey key() {
        try {
            byte[] k = MessageDigest.getInstance("SHA-256").digest(secret.getBytes(StandardCharsets.UTF_8));
            return new SecretKeySpec(k, "HmacSHA256");
        } catch (Exception e) { throw new RuntimeException(e); }
    }

    public String generateAccess(UUID userId, String email, List<String> roles) {
        Date now = new Date();
        return Jwts.builder()
                .subject(userId.toString())
                .claim("email", email)
                .claim("roles", roles)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + accessTtl*1000))
                .signWith(key())
                .compact();
    }

    public String generateRefresh(UUID userId) {
        Date now = new Date();
        return Jwts.builder()
                .subject(userId.toString())
                .claim("type","refresh")
                .issuedAt(now)
                .expiration(new Date(now.getTime() + refreshTtl*1000))
                .signWith(key())
                .compact();
    }

    public Claims parse(String token) {
        return Jwts.parser().verifyWith(key()).build().parseSignedClaims(token).getPayload();
    }

    public long accessTtl() { return accessTtl; }
    public long refreshTtl() { return refreshTtl; }
}
