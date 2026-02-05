package com.varios.toolshub.config;

import com.varios.toolshub.user.Role;
import com.varios.toolshub.user.User;
import com.varios.toolshub.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        userRepository.findByUsername("test").orElseGet(() -> {
            User admin = new User();
            admin.setUsername("test");
            admin.setPasswordHash(passwordEncoder.encode("test123456"));
            admin.setRole(Role.ADMIN);
            admin.setEnabled(true);
            return userRepository.save(admin);
        });
    }
}
