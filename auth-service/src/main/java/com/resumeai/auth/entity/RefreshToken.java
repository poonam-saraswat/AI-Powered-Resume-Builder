package com.resumeai.auth.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.UUID;

@Entity @Table(name="refresh_tokens")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RefreshToken {
    @Id @GeneratedValue private UUID id;
    @Column(nullable=false, unique=true, length=512) private String token;
    @Column(name="user_id", nullable=false) private UUID userId;
    @Column(nullable=false) private Instant expiresAt;
    private boolean revoked;
}
