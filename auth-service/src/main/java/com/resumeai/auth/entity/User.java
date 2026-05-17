package com.resumeai.auth.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.UUID;

@Entity @Table(name="users")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class User {
    @Id @GeneratedValue private UUID id;
    @Column(unique=true, nullable=false) private String email;
    private String passwordHash;
    private String fullName;
    private String avatarUrl;
    @Enumerated(EnumType.STRING) private AuthProvider provider;
    private String providerId;
    private boolean emailVerified;
    @Column(updatable=false) private Instant createdAt;
    private Instant updatedAt;
    @PrePersist void pre(){ createdAt=Instant.now(); updatedAt=createdAt; if(provider==null) provider=AuthProvider.LOCAL; }
    @PreUpdate  void upd(){ updatedAt=Instant.now(); }
}
