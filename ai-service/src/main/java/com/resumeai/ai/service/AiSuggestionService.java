package com.resumeai.ai.service;
import com.resumeai.ai.entity.AiSuggestion;
import com.resumeai.ai.repository.AiSuggestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List; import java.util.UUID;

@Service @RequiredArgsConstructor
public class AiSuggestionService {
    private final AiSuggestionRepository repo;
    public List<AiSuggestion> listByUser(UUID userId){ return repo.findByUserId(userId); }
    public AiSuggestion get(UUID id){ return repo.findById(id).orElseThrow(); }
    public AiSuggestion create(AiSuggestion e){ return repo.save(e); }
    public AiSuggestion update(UUID id, AiSuggestion e){ e.setId(id); return repo.save(e); }
    public void delete(UUID id){ repo.deleteById(id); }
}
