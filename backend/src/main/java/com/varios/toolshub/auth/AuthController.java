package com.varios.toolshub.auth;

import com.varios.toolshub.common.CurrentUser;
import com.varios.toolshub.security.AuthUser;
import com.varios.toolshub.security.CsrfCookieFilter;
import com.varios.toolshub.security.JwtAuthenticationFilter;
import com.varios.toolshub.security.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final JwtService jwtService;
    private final LoginAttemptService loginAttemptService;
    private final CurrentUser currentUser;
    private final boolean cookieSecure;

    public AuthController(AuthService authService, JwtService jwtService, LoginAttemptService loginAttemptService,
                          CurrentUser currentUser, @Value("${app.auth.cookie-secure}") boolean cookieSecure) {
        this.authService = authService;
        this.jwtService = jwtService;
        this.loginAttemptService = loginAttemptService;
        this.currentUser = currentUser;
        this.cookieSecure = cookieSecure;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthDtos.LoginRequest request, HttpServletRequest httpRequest, HttpServletResponse response) {
        String key = request.username() + ":" + httpRequest.getRemoteAddr();
        if (loginAttemptService.isLocked(key)) {
            return ResponseEntity.status(429).body(Map.of("error", "Too many failed attempts"));
        }
        try {
            AuthUser user = authService.login(request.username(), request.password());
            loginAttemptService.success(key);
            addCookie(response, JwtAuthenticationFilter.AUTH_COOKIE, jwtService.generateToken(user), true);
            addCookie(response, CsrfCookieFilter.CSRF_COOKIE, randomToken(), false);
            return ResponseEntity.ok(Map.of("username", user.username(), "role", user.role().name()));
        } catch (IllegalArgumentException ex) {
            loginAttemptService.fail(key);
            return ResponseEntity.status(401).body(Map.of("error", ex.getMessage()));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        addCookie(response, JwtAuthenticationFilter.AUTH_COOKIE, "", true, 0);
        addCookie(response, CsrfCookieFilter.CSRF_COOKIE, "", false, 0);
        return ResponseEntity.ok(Map.of("message", "Logged out"));
    }

    @GetMapping("/me")
    public Map<String, Object> me() {
        AuthUser user = currentUser.get();
        return Map.of("id", user.id(), "username", user.username(), "role", user.role().name());
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody AuthDtos.ChangePasswordRequest request) {
        authService.changePassword(currentUser.get().id(), request.currentPassword(), request.newPassword());
        return ResponseEntity.ok(Map.of("message", "Password changed"));
    }

    @PostMapping("/reset-password/complete")
    public ResponseEntity<?> completeReset(@Valid @RequestBody AuthDtos.CompleteResetRequest request) {
        authService.completeReset(request.token(), request.newPassword());
        return ResponseEntity.ok(Map.of("message", "Password reset done"));
    }

    private String randomToken() {
        byte[] bytes = new byte[24];
        new SecureRandom().nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private void addCookie(HttpServletResponse response, String name, String value, boolean httpOnly) {
        addCookie(response, name, value, httpOnly, -1);
    }

    private void addCookie(HttpServletResponse response, String name, String value, boolean httpOnly, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setHttpOnly(httpOnly);
        cookie.setSecure(cookieSecure);
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }
}
