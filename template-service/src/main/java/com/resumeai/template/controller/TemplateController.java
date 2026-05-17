package com.resumeai.template.controller;

import com.resumeai.template.entity.Template;
import com.resumeai.template.service.TemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/templates")
@RequiredArgsConstructor
public class TemplateController {
    private final TemplateService svc;

    /** Returns samples + user's own templates (samples always included). */
    @GetMapping
    public List<Template> list(@RequestHeader(value="X-User-Id", required=false) String uid){
        List<Template> all = new ArrayList<>(svc.listSamples());
        if (uid != null) all.addAll(svc.listByUser(UUID.fromString(uid)));
        return all;
    }

    /** Public samples only — no auth required. */
    @GetMapping("/samples")
    public List<Template> samples(){ return svc.listSamples(); }

    @GetMapping("/{id}") public Template get(@PathVariable UUID id){ return svc.get(id); }

    @PostMapping
    public ResponseEntity<Template> create(@RequestHeader("X-User-Id") String uid, @RequestBody Template e){
        e.setUserId(UUID.fromString(uid));
        e.setSample(false);
        return ResponseEntity.ok(svc.create(e));
    }

    @PutMapping("/{id}") public Template update(@PathVariable UUID id, @RequestBody Template e){ return svc.update(id, e); }
    @DeleteMapping("/{id}") public ResponseEntity<?> delete(@PathVariable UUID id){ svc.delete(id); return ResponseEntity.noContent().build(); }
}
