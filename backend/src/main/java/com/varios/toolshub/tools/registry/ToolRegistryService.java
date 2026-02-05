package com.varios.toolshub.tools.registry;

import com.varios.toolshub.user.Role;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ToolRegistryService {
    private final List<ToolDefinition> tools = List.of(
            new ToolDefinition("vault", "Password Vault Lite", "Save encrypted website credentials", "/tools/vault", Role.USER),
            new ToolDefinition("todo", "Todo Memo", "Track quick todo notes", "/tools/todo", Role.USER),
            new ToolDefinition("admin-users", "Admin User Manager", "Create, disable and reset users", "/admin/users", Role.ADMIN)
    );

    public List<ToolDefinition> listFor(Role role) {
        return tools.stream().filter(t -> role == Role.ADMIN || t.minRole() == Role.USER).toList();
    }
}
