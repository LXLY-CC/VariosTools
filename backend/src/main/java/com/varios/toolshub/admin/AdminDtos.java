package com.varios.toolshub.admin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.Instant;

public class AdminDtos {
    public record CreateUserRequest(@NotBlank String username,
                                    @NotBlank @Size(min = 8, max = 100) String password,
                                    @NotBlank String role) {}
    public record UpdateEnabledRequest(@NotNull Boolean enabled) {}
    public record ResetPasswordRequest(@NotBlank @Size(min = 8, max = 100) String tempPassword) {}
    public record UserSummaryResponse(Long id, String username, String role, boolean enabled, Instant createdAt) {}
}
