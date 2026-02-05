package com.varios.toolshub.config;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SpaForwardController {
    @GetMapping({"/", "/login", "/dashboard", "/tools/**", "/admin/**", "/reset-password"})
    public String forward() {
        return "forward:/index.html";
    }
}
