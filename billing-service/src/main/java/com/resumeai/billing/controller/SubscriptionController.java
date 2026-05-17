package com.resumeai.billing.controller;
import com.resumeai.billing.entity.Subscription;
import com.resumeai.billing.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List; import java.util.UUID;

@RestController
@RequestMapping("/api/billing")
@RequiredArgsConstructor
public class SubscriptionController {
    private final SubscriptionService svc;

    @GetMapping
    public List<Subscription> list(@RequestHeader(value="X-User-Id", required=false) String uid){
        return uid==null ? List.of() : svc.listByUser(UUID.fromString(uid));
    }
    @GetMapping("/{id}") public Subscription get(@PathVariable UUID id){ return svc.get(id); }

    @PostMapping
    public ResponseEntity<Subscription> create(@RequestHeader("X-User-Id") String uid, @RequestBody Subscription e){
        e.setUserId(UUID.fromString(uid));
        return ResponseEntity.ok(svc.create(e));
    }
    @PutMapping("/{id}") public Subscription update(@PathVariable UUID id, @RequestBody Subscription e){ return svc.update(id, e); }
    @DeleteMapping("/{id}") public ResponseEntity<?> delete(@PathVariable UUID id){ svc.delete(id); return ResponseEntity.noContent().build(); }
}
