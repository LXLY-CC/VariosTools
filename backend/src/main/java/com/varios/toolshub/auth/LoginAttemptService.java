package com.varios.toolshub.auth;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class LoginAttemptService {
    private static final int MAX_FAIL = 5;
    private static final long LOCK_MINUTES = 15;
    private final Map<String, Attempt> attempts = new ConcurrentHashMap<>();

    public boolean isLocked(String key) {
        Attempt a = attempts.get(key);
        return a != null && a.lockedUntil != null && a.lockedUntil.isAfter(Instant.now());
    }

    public void success(String key) { attempts.remove(key); }

    public void fail(String key) {
        attempts.compute(key, (k, v) -> {
            Attempt a = v == null ? new Attempt() : v;
            a.failures++;
            if (a.failures >= MAX_FAIL) {
                a.lockedUntil = Instant.now().plusSeconds(LOCK_MINUTES * 60);
                a.failures = 0;
            }
            return a;
        });
    }

    private static class Attempt {
        int failures;
        Instant lockedUntil;
    }
}
