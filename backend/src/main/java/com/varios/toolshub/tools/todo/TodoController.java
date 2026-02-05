package com.varios.toolshub.tools.todo;

import com.varios.toolshub.common.CurrentUser;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/tools/todo")
public class TodoController {
    private final TodoService todoService;
    private final CurrentUser currentUser;

    public TodoController(TodoService todoService, CurrentUser currentUser) {
        this.todoService = todoService;
        this.currentUser = currentUser;
    }

    @GetMapping
    public Object list() { return todoService.list(currentUser.get().id()); }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody TodoDtos.TodoCreateRequest request) {
        todoService.create(currentUser.get().id(), request);
        return ResponseEntity.ok(Map.of("message", "Created"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody TodoDtos.TodoUpdateRequest request) {
        todoService.update(currentUser.get().id(), id, request);
        return ResponseEntity.ok(Map.of("message", "Updated"));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> toggle(@PathVariable Long id, @RequestParam boolean completed) {
        todoService.toggle(currentUser.get().id(), id, completed);
        return ResponseEntity.ok(Map.of("message", "Updated"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        todoService.delete(currentUser.get().id(), id);
        return ResponseEntity.ok(Map.of("message", "Deleted"));
    }
}
