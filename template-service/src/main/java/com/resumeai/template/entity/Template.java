package com.resumeai.template.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.UUID;

@Entity @Table(name="templates")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Template {
    @Id @GeneratedValue private UUID id;

    // nullable now — sample templates have no owner
    @Column(name="user_id") private UUID userId;

    private String name;
    private String category;
    private String previewUrl;

    @Column(columnDefinition="text") private String htmlTemplate;

    @Column(name="is_sample", nullable=false)
    private boolean isSample = false;

    @Column(updatable=false) private Instant createdAt;
    private Instant updatedAt;

    @PrePersist void p(){ createdAt=Instant.now(); updatedAt=createdAt; }
    @PreUpdate  void u(){ updatedAt=Instant.now(); }
}
