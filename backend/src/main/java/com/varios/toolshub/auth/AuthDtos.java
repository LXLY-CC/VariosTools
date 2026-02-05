package com.varios.toolshub.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AuthDtos {
    public record LoginRequest(@NotBlank String username, @NotBlank String password) {}
    public record ChangePasswordRequest(@NotBlank String currentPassword, @NotBlank @Size(min = 8, max = 100) String newPassword) {}
    public record CompleteResetRequest(@NotBlank String token, @NotBlank @Size(min = 8, max = 100) String newPassword) {}
}
