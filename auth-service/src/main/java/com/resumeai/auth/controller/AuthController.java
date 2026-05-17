package com.resumeai.auth.controller;

import com.resumeai.auth.dto.AuthDtos.*;
import com.resumeai.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService auth;

    @PostMapping("/register")
    public ResponseEntity<TokenResponse> register(@Valid @RequestBody RegisterRequest r) {
        return ResponseEntity.ok(auth.register(r));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest r) {
        return ResponseEntity.ok(auth.login(r));
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(@Valid @RequestBody RefreshRequest r) {
        return ResponseEntity.ok(auth.refresh(r.refreshToken()));
    }

    @GetMapping("/me")
    public ResponseEntity<MeResponse> me(@AuthenticationPrincipal Object principal) {
        UUID id = UUID.fromString(principal.toString());
        return ResponseEntity.ok(auth.me(id));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgot(@Valid @RequestBody ForgotRequest r) {
        // TODO: emit Kafka event 'password.reset.requested' for notification-service
        return ResponseEntity.ok().body("If the email exists, a reset link has been sent");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> reset(@Valid @RequestBody ResetRequest r) {
        // TODO: validate reset token and set new password
        return ResponseEntity.ok().build();
    }
}
