package com.resumeai.auth.repository;
import com.resumeai.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional; import java.util.UUID;
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
    Optional<User> findByProviderAndProviderId(com.resumeai.auth.entity.AuthProvider p, String pid);
    boolean existsByEmail(String email);
}
