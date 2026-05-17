package com.resumeai.auth.entity;
import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity @Table(name="user_roles", uniqueConstraints=@UniqueConstraint(columnNames={"user_id","role"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class UserRole {
    @Id @GeneratedValue private UUID id;
    @Column(name="user_id", nullable=false) private UUID userId;
    @Enumerated(EnumType.STRING) @Column(nullable=false) private Role role;
    public enum Role { USER, ADMIN, MODERATOR }
}
