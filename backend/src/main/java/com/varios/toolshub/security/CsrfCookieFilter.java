package com.varios.toolshub.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Component
public class CsrfCookieFilter extends OncePerRequestFilter {
    public static final String CSRF_COOKIE = "XSRF-TOKEN";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (!HttpMethod.GET.matches(request.getMethod())
                && !HttpMethod.HEAD.matches(request.getMethod())
                && !HttpMethod.OPTIONS.matches(request.getMethod())) {
            String header = request.getHeader("X-CSRF-Token");
            String cookieToken = null;
            if (request.getCookies() != null) {
                cookieToken = Arrays.stream(request.getCookies())
                        .filter(c -> CSRF_COOKIE.equals(c.getName()))
                        .map(Cookie::getValue)
                        .findFirst().orElse(null);
            }
            String path = request.getRequestURI();
            boolean skip = path.startsWith("/api/auth/login") || path.startsWith("/api/auth/reset-password/complete");
            if (!skip && (cookieToken == null || !cookieToken.equals(header))) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write("Invalid CSRF token");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}
