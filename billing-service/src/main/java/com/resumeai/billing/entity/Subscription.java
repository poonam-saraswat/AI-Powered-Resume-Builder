package com.resumeai.billing.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.UUID;

@Entity @Table(name="billing")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Subscription {
    @Id @GeneratedValue private UUID id;
    @Column(name="user_id", nullable=false) private UUID userId;
    private String plan;
    private String status;
    private Integer aiQuotaUsed;
    private Integer aiQuotaLimit;
    private String stripeCustomerId;
    private String stripeSubscriptionId;
    @Column(updatable=false) private Instant createdAt;
    private Instant updatedAt;
    @PrePersist void p(){ createdAt=Instant.now(); updatedAt=createdAt; }
    @PreUpdate  void u(){ updatedAt=Instant.now(); }
}
