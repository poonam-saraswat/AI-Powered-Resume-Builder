package com.resumeai.notification.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.UUID;

@Entity @Table(name="notifications")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Notification {
    @Id @GeneratedValue private UUID id;
    @Column(name="user_id", nullable=false) private UUID userId;
    private String type;
    private String title;
    @Column(columnDefinition="text") private String body;
    private boolean read;
    @Column(updatable=false) private Instant createdAt;
    private Instant updatedAt;
    @PrePersist void p(){ createdAt=Instant.now(); updatedAt=createdAt; }
    @PreUpdate  void u(){ updatedAt=Instant.now(); }
}
