package com.resumeai.auth.dto;
import jakarta.validation.constraints.*;
public class AuthDtos {
    public record RegisterRequest(@Email @NotBlank String email, @NotBlank @Size(min=8) String password, @NotBlank String fullName) {}
    public record LoginRequest(@Email @NotBlank String email, @NotBlank String password) {}
    public record RefreshRequest(@NotBlank String refreshToken) {}
    public record ForgotRequest(@Email @NotBlank String email) {}
    public record ResetRequest(@NotBlank String token, @NotBlank @Size(min=8) String newPassword) {}
    public record TokenResponse(String accessToken, String refreshToken, String tokenType, long expiresIn) {}
    public record MeResponse(java.util.UUID id, String email, String fullName, String avatarUrl, java.util.List<String> roles, String provider) {}
}
