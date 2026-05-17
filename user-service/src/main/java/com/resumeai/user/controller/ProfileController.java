package com.resumeai.user.controller;
import com.resumeai.user.entity.Profile;
import com.resumeai.user.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List; import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileService svc;

    @GetMapping
    public List<Profile> list(@RequestHeader(value="X-User-Id", required=false) String uid){
        return uid==null ? List.of() : svc.listByUser(UUID.fromString(uid));
    }
    @GetMapping("/{id}") public Profile get(@PathVariable UUID id){ return svc.get(id); }

    @PostMapping
    public ResponseEntity<Profile> create(@RequestHeader("X-User-Id") String uid, @RequestBody Profile e){
        e.setUserId(UUID.fromString(uid));
        return ResponseEntity.ok(svc.create(e));
    }
    @PutMapping("/{id}") public Profile update(@PathVariable UUID id, @RequestBody Profile e){ return svc.update(id, e); }
    @DeleteMapping("/{id}") public ResponseEntity<?> delete(@PathVariable UUID id){ svc.delete(id); return ResponseEntity.noContent().build(); }
}
