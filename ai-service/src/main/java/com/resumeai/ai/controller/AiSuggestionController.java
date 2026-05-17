package com.resumeai.ai.controller;
import com.resumeai.ai.entity.AiSuggestion;
import com.resumeai.ai.service.AiSuggestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List; import java.util.UUID;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiSuggestionController {
    private final AiSuggestionService svc;

    @GetMapping
    public List<AiSuggestion> list(@RequestHeader(value="X-User-Id", required=false) String uid){
        return uid==null ? List.of() : svc.listByUser(UUID.fromString(uid));
    }
    @GetMapping("/{id}") public AiSuggestion get(@PathVariable UUID id){ return svc.get(id); }

    @PostMapping
    public ResponseEntity<AiSuggestion> create(@RequestHeader("X-User-Id") String uid, @RequestBody AiSuggestion e){
        e.setUserId(UUID.fromString(uid));
        return ResponseEntity.ok(svc.create(e));
    }
    @PutMapping("/{id}") public AiSuggestion update(@PathVariable UUID id, @RequestBody AiSuggestion e){ return svc.update(id, e); }
    @DeleteMapping("/{id}") public ResponseEntity<?> delete(@PathVariable UUID id){ svc.delete(id); return ResponseEntity.noContent().build(); }
}
