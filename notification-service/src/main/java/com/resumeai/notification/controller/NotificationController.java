package com.resumeai.notification.controller;
import com.resumeai.notification.entity.Notification;
import com.resumeai.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List; import java.util.UUID;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService svc;

    @GetMapping
    public List<Notification> list(@RequestHeader(value="X-User-Id", required=false) String uid){
        return uid==null ? List.of() : svc.listByUser(UUID.fromString(uid));
    }
    @GetMapping("/{id}") public Notification get(@PathVariable UUID id){ return svc.get(id); }

    @PostMapping
    public ResponseEntity<Notification> create(@RequestHeader("X-User-Id") String uid, @RequestBody Notification e){
        e.setUserId(UUID.fromString(uid));
        return ResponseEntity.ok(svc.create(e));
    }
    @PutMapping("/{id}") public Notification update(@PathVariable UUID id, @RequestBody Notification e){ return svc.update(id, e); }
    @DeleteMapping("/{id}") public ResponseEntity<?> delete(@PathVariable UUID id){ svc.delete(id); return ResponseEntity.noContent().build(); }
}
