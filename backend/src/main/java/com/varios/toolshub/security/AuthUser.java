package com.varios.toolshub.security;

import com.varios.toolshub.user.Role;

public record AuthUser(Long id, String username, Role role) {}
