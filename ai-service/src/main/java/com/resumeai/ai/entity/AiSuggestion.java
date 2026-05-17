package com.resumeai.ai.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.UUID;

@Entity @Table(name="ai")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AiSuggestion {
    @Id @GeneratedValue private UUID id;
    @Column(name="user_id", nullable=false) private UUID userId;
    private String resumeId;
    private String kind;
    @Column(columnDefinition="text") private String prompt;
    @Column(columnDefinition="text") private String response;
    @Column(updatable=false) private Instant createdAt;
    private Instant updatedAt;
    @PrePersist void p(){ createdAt=Instant.now(); updatedAt=createdAt; }
    @PreUpdate  void u(){ updatedAt=Instant.now(); }
}
