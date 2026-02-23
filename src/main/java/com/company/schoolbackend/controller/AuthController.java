package com.company.schoolbackend.controller;

import com.company.schoolbackend.dto.AuthLoginRequest;
import com.company.schoolbackend.dto.AuthLoginResponse;
import com.company.schoolbackend.dto.AuthRegisterRequest;
import com.company.schoolbackend.dto.UserProfile;
import com.company.schoolbackend.service.AuthService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final boolean registrationEnabled;

    public AuthController(
            AuthService authService,
            @Value("${school.registration.enabled:true}") boolean registrationEnabled
    ) {
        this.authService = authService;
        this.registrationEnabled = registrationEnabled;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthLoginRequest request) {
        try {
            AuthLoginResponse response = authService.login(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(java.util.Map.of("error", ex.getMessage()));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthRegisterRequest request) {
        if (!registrationEnabled) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(java.util.Map.of("error", "Registration disabled"));
        }
        try {
            UserProfile profile = authService.register(request);
            return ResponseEntity.ok(profile);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(java.util.Map.of("error", ex.getMessage()));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        authService.logout(extractToken(authHeader));
        return ResponseEntity.ok(java.util.Map.of("ok", true));
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        UserProfile profile = authService.verify(extractToken(authHeader));
        if (profile == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(java.util.Map.of("error", "Unauthorized"));
        }
        return ResponseEntity.ok(profile);
    }

    private String extractToken(String header) {
        if (header == null || header.isBlank()) {
            return null;
        }
        if (header.toLowerCase().startsWith("bearer ")) {
            return header.substring(7);
        }
        return header;
    }
}
