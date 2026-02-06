package com.varios.toolshub.tools.registry;

import com.varios.toolshub.user.Role;

public record ToolDefinition(String code, String name, String description, String route, Role minRole) {}
