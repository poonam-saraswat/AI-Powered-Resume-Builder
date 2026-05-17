package com.resumeai.auth.service;

import com.resumeai.auth.dto.AuthDtos.*;
import com.resumeai.auth.entity.*;
import com.resumeai.auth.repository.*;
import com.resumeai.auth.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service @RequiredArgsConstructor
public class AuthService {
    private final UserRepository users;
    private final UserRoleRepository roles;
    private final RefreshTokenRepository refreshRepo;
    private final PasswordEncoder encoder;
    private final JwtService jwt;

    @Transactional
    public TokenResponse register(RegisterRequest r) {
        if (users.existsByEmail(r.email())) throw new IllegalArgumentException("Email already in use");
        User u = users.save(User.builder()
                .email(r.email())
                .passwordHash(encoder.encode(r.password()))
                .fullName(r.fullName())
                .provider(AuthProvider.LOCAL)
                .emailVerified(false)
                .build());
        roles.save(UserRole.builder().userId(u.getId()).role(UserRole.Role.USER).build());
        return issueTokens(u);
    }

    @Transactional
    public TokenResponse login(LoginRequest r) {
        User u = users.findByEmail(r.email()).orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));
        if (u.getPasswordHash() == null || !encoder.matches(r.password(), u.getPasswordHash()))
            throw new IllegalArgumentException("Invalid credentials");
        return issueTokens(u);
    }

    @Transactional
    public TokenResponse refresh(String refreshToken) {
        RefreshToken stored = refreshRepo.findByToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("Invalid refresh token"));
        if (stored.isRevoked() || stored.getExpiresAt().isBefore(Instant.now()))
            throw new IllegalArgumentException("Refresh token expired");
        stored.setRevoked(true);
        refreshRepo.save(stored);
        User u = users.findById(stored.getUserId()).orElseThrow();
        return issueTokens(u);
    }

    public TokenResponse issueTokens(User u) {
        List<String> roleNames = roles.findByUserId(u.getId()).stream().map(r -> r.getRole().name()).toList();
        String access = jwt.generateAccess(u.getId(), u.getEmail(), roleNames);
        String refresh = jwt.generateRefresh(u.getId());
        refreshRepo.save(RefreshToken.builder()
                .token(refresh).userId(u.getId())
                .expiresAt(Instant.now().plusSeconds(jwt.refreshTtl()))
                .revoked(false).build());
        return new TokenResponse(access, refresh, "Bearer", jwt.accessTtl());
    }

    @Transactional
    public User upsertOAuthUser(AuthProvider provider, String providerId, String email, String name, String avatar) {
        return users.findByProviderAndProviderId(provider, providerId)
                .or(() -> users.findByEmail(email).map(existing -> {
                    existing.setProvider(provider);
                    existing.setProviderId(providerId);
                    if (existing.getAvatarUrl() == null) existing.setAvatarUrl(avatar);
                    return users.save(existing);
                }))
                .orElseGet(() -> {
                    User created = users.save(User.builder()
                            .email(email).fullName(name).avatarUrl(avatar)
                            .provider(provider).providerId(providerId)
                            .emailVerified(true).build());
                    roles.save(UserRole.builder().userId(created.getId()).role(UserRole.Role.USER).build());
                    return created;
                });
    }

    public MeResponse me(UUID userId) {
        User u = users.findById(userId).orElseThrow();
        List<String> r = roles.findByUserId(userId).stream().map(x -> x.getRole().name()).toList();
        return new MeResponse(u.getId(), u.getEmail(), u.getFullName(), u.getAvatarUrl(), r, u.getProvider().name());
    }
}
