package com.varios.toolshub.tools.todo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.Instant;

public class TodoDtos {
    public record TodoCreateRequest(@NotBlank @Size(max = 4000) String content) {}
    public record TodoUpdateRequest(@NotBlank @Size(max = 4000) String content, boolean completed) {}
    public record TodoResponse(Long id, String content, boolean completed, Instant createdAt, Instant updatedAt) {}
}
