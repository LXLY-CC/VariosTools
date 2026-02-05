package com.varios.toolshub.admin;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/users")
    public Object listUsers() { return adminService.listUsers(); }

    @PostMapping("/users")
    public ResponseEntity<?> createUser(@Valid @RequestBody AdminDtos.CreateUserRequest request) {
        adminService.createUser(request);
        return ResponseEntity.ok(Map.of("message", "User created"));
    }

    @PatchMapping("/users/{id}/enabled")
    public ResponseEntity<?> setEnabled(@PathVariable Long id, @Valid @RequestBody AdminDtos.UpdateEnabledRequest request) {
        adminService.setEnabled(id, request.enabled());
        return ResponseEntity.ok(Map.of("message", "Updated"));
    }

    @PostMapping("/users/{id}/reset-password")
    public ResponseEntity<?> resetPassword(@PathVariable Long id, @Valid @RequestBody AdminDtos.ResetPasswordRequest request) {
        adminService.resetPassword(id, request.tempPassword());
        return ResponseEntity.ok(Map.of("message", "Password reset"));
    }

    @PostMapping("/users/{id}/reset-token")
    public ResponseEntity<?> generateResetToken(@PathVariable Long id) {
        return ResponseEntity.ok(Map.of("token", adminService.generateResetToken(id)));
    }
}
