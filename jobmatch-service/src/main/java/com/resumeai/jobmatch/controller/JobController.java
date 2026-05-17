package com.resumeai.jobmatch.controller;

import com.resumeai.jobmatch.dto.JobSearchResult;
import com.resumeai.jobmatch.entity.Job;
import com.resumeai.jobmatch.service.JobSearchService;
import com.resumeai.jobmatch.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService svc;
    private final JobSearchService searchSvc;

    // ✅ NEW: real job search (LinkedIn / Indeed / Glassdoor via JSearch, or Remotive fallback)
    @GetMapping("/search")
    public List<JobSearchResult> search(
            @RequestParam(value = "query", required = false, defaultValue = "") String query,
            @RequestParam(value = "location", required = false, defaultValue = "") String location,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page) {
        return searchSvc.search(query, location, page);
    }

    @GetMapping
    public List<Job> list(@RequestHeader(value = "X-User-Id", required = false) String uid) {
        return uid == null ? List.of() : svc.listByUser(UUID.fromString(uid));
    }

    @GetMapping("/{id}")
    public Job get(@PathVariable UUID id) { return svc.get(id); }

    @PostMapping
    public ResponseEntity<Job> create(@RequestHeader("X-User-Id") String uid, @RequestBody Job e) {
        e.setUserId(UUID.fromString(uid));
        return ResponseEntity.ok(svc.create(e));
    }

    @PutMapping("/{id}")
    public Job update(@PathVariable UUID id, @RequestBody Job e) { return svc.update(id, e); }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        svc.delete(id);
        return ResponseEntity.noContent().build();
    }
}
