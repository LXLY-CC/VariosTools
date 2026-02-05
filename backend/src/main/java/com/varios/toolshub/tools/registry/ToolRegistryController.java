package com.varios.toolshub.tools.registry;

import com.varios.toolshub.common.CurrentUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tools")
public class ToolRegistryController {
    private final ToolRegistryService registryService;
    private final CurrentUser currentUser;

    public ToolRegistryController(ToolRegistryService registryService, CurrentUser currentUser) {
        this.registryService = registryService;
        this.currentUser = currentUser;
    }

    @GetMapping
    public Object listTools() {
        return registryService.listFor(currentUser.get().role());
    }
}
