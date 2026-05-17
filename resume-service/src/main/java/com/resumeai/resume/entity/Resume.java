package com.resumeai.resume.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.UUID;

@Entity @Table(name="resumes")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Resume {
    @Id @GeneratedValue private UUID id;
    @Column(name="user_id", nullable=false) private UUID userId;
    private String title;
    @Column(columnDefinition="text") private String contentJson;
    private String templateId;
    private Integer version;
    @Column(updatable=false) private Instant createdAt;
    private Instant updatedAt;
    @PrePersist void p(){ createdAt=Instant.now(); updatedAt=createdAt; }
    @PreUpdate  void u(){ updatedAt=Instant.now(); }
}
