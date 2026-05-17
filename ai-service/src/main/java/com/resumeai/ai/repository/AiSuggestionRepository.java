package com.resumeai.ai.repository;
import com.resumeai.ai.entity.AiSuggestion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List; import java.util.UUID;
public interface AiSuggestionRepository extends JpaRepository<AiSuggestion, UUID> {
    List<AiSuggestion> findByUserId(UUID userId);
}
