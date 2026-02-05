package com.varios.toolshub.tools.vault;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.Instant;

public class VaultDtos {
    public record VaultUpsertRequest(@NotBlank @Size(max = 512) String websiteUrl,
                                     @NotBlank @Size(max = 255) String username,
                                     @NotBlank @Size(max = 500) String password,
                                     @Size(max = 1000) String description) {}

    public record VaultEntryResponse(Long id, String websiteUrl, String username, String password, String description,
                                     Instant createdAt, Instant updatedAt) {}
}
