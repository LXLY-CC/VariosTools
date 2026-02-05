package com.varios.toolshub.admin;

import com.varios.toolshub.auth.PasswordResetToken;
import com.varios.toolshub.auth.PasswordResetTokenRepository;
import com.varios.toolshub.user.Role;
import com.varios.toolshub.user.User;
import com.varios.toolshub.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Service
public class AdminService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordResetTokenRepository tokenRepository;
    private final long resetTokenExpMin;

    public AdminService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                        PasswordResetTokenRepository tokenRepository,
                        @Value("${app.reset.token-expiration-minutes}") long resetTokenExpMin) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenRepository = tokenRepository;
        this.resetTokenExpMin = resetTokenExpMin;
    }

    public List<Map<String, Object>> listUsers() {
        return userRepository.findAll().stream().map(u -> Map.of(
                "id", u.getId(), "username", u.getUsername(), "role", u.getRole().name(), "enabled", u.isEnabled(),
                "createdAt", u.getCreatedAt())).toList();
    }

    @Transactional
    public void createUser(AdminDtos.CreateUserRequest request) {
        if (userRepository.existsByUsername(request.username())) throw new IllegalArgumentException("Username exists");
        User u = new User();
        u.setUsername(request.username());
        u.setPasswordHash(passwordEncoder.encode(request.password()));
        u.setRole(Role.valueOf(request.role().toUpperCase()));
        u.setEnabled(true);
        userRepository.save(u);
    }

    @Transactional
    public void setEnabled(Long id, boolean enabled) {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found"));
        if ("test".equals(user.getUsername()) && !enabled) throw new IllegalArgumentException("Cannot disable super admin");
        user.setEnabled(enabled);
    }

    @Transactional
    public void resetPassword(Long id, String tempPassword) {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found"));
        user.setPasswordHash(passwordEncoder.encode(tempPassword));
    }

    @Transactional
    public String generateResetToken(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found"));
        PasswordResetToken token = new PasswordResetToken();
        token.setUser(user);
        token.setToken(randomToken());
        token.setExpiresAt(Instant.now().plusSeconds(resetTokenExpMin * 60));
        tokenRepository.save(token);
        return token.getToken();
    }

    private String randomToken() {
        byte[] bytes = new byte[24];
        new SecureRandom().nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}
