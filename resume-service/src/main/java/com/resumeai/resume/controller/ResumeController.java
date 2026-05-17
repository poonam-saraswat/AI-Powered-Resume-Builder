package com.resumeai.resume.controller;
import com.resumeai.resume.entity.Resume;
import com.resumeai.resume.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List; import java.util.UUID;

@RestController
@RequestMapping("/api/resumes")
@RequiredArgsConstructor
public class ResumeController {
    private final ResumeService svc;

    @GetMapping
    public List<Resume> list(@RequestHeader(value="X-User-Id", required=false) String uid){
        return uid==null ? List.of() : svc.listByUser(UUID.fromString(uid));
    }
    @GetMapping("/{id}") public Resume get(@PathVariable UUID id){ return svc.get(id); }

    @PostMapping
    public ResponseEntity<Resume> create(@RequestHeader("X-User-Id") String uid, @RequestBody Resume e){
        e.setUserId(UUID.fromString(uid));
        return ResponseEntity.ok(svc.create(e));
    }
    @PutMapping("/{id}") public Resume update(@PathVariable UUID id, @RequestBody Resume e){ return svc.update(id, e); }
    @DeleteMapping("/{id}") public ResponseEntity<?> delete(@PathVariable UUID id){ svc.delete(id); return ResponseEntity.noContent().build(); }
}
