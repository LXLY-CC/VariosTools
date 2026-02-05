package com.varios.toolshub.tools.vault;

import com.varios.toolshub.common.CurrentUser;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/tools/vault")
public class VaultController {
    private final VaultService vaultService;
    private final CurrentUser currentUser;

    public VaultController(VaultService vaultService, CurrentUser currentUser) {
        this.vaultService = vaultService;
        this.currentUser = currentUser;
    }

    @GetMapping
    public Object list(@RequestParam(required = false) String q) {
        return vaultService.list(currentUser.get().id(), q);
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody VaultDtos.VaultUpsertRequest request) {
        vaultService.create(currentUser.get().id(), request);
        return ResponseEntity.ok(Map.of("message", "Created"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody VaultDtos.VaultUpsertRequest request) {
        vaultService.update(currentUser.get().id(), id, request);
        return ResponseEntity.ok(Map.of("message", "Updated"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        vaultService.delete(currentUser.get().id(), id);
        return ResponseEntity.ok(Map.of("message", "Deleted"));
    }
}
