package com.resumeai.user.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.UUID;

@Entity @Table(name="users")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Profile {
    @Id @GeneratedValue private UUID id;
    @Column(name="user_id", nullable=false) private UUID userId;
    private String displayName;
    private String headline;
    private String bio;
    private String location;
    private String websiteUrl;
    @Column(updatable=false) private Instant createdAt;
    private Instant updatedAt;
    @PrePersist void p(){ createdAt=Instant.now(); updatedAt=createdAt; }
    @PreUpdate  void u(){ updatedAt=Instant.now(); }
}
