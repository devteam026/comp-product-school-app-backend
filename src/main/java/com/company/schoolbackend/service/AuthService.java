package com.company.schoolbackend.service;

import com.company.schoolbackend.dto.AuthLoginRequest;
import com.company.schoolbackend.dto.AuthLoginResponse;
import com.company.schoolbackend.dto.AuthRegisterRequest;
import com.company.schoolbackend.dto.UserProfile;
import com.company.schoolbackend.entity.AppUser;
import com.company.schoolbackend.entity.SessionToken;
import com.company.schoolbackend.entity.UserRole;
import com.company.schoolbackend.repository.AppUserRepository;
import com.company.schoolbackend.repository.SessionTokenRepository;
import com.company.schoolbackend.repository.TeacherClassRepository;
import java.time.OffsetDateTime;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final AppUserRepository appUserRepository;
    private final TeacherClassRepository teacherClassRepository;
    private final SessionTokenRepository sessionTokenRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
            AppUserRepository appUserRepository,
            TeacherClassRepository teacherClassRepository,
            SessionTokenRepository sessionTokenRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.appUserRepository = appUserRepository;
        this.teacherClassRepository = teacherClassRepository;
        this.sessionTokenRepository = sessionTokenRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthLoginResponse login(AuthLoginRequest request) {
        if (request == null || request.getUsername() == null || request.getPassword() == null
                || request.getRole() == null) {
            throw new IllegalArgumentException("Missing credentials");
        }
        String username = request.getUsername().trim().toLowerCase(Locale.ROOT);
        if (username.isEmpty()) {
            throw new IllegalArgumentException("Missing credentials");
        }
        UserRole role = UserRole.valueOf(request.getRole().trim().toLowerCase(Locale.ROOT));
        Optional<AppUser> userOpt = appUserRepository.findByUsernameAndRole(username, role);
        AppUser user = userOpt.orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));
        if (!user.isActive() || !passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        String token = UUID.randomUUID().toString().replace("-", "");
        SessionToken sessionToken = new SessionToken();
        sessionToken.setUserId(user.getId());
        sessionToken.setToken(token);
        sessionToken.setCreatedAt(OffsetDateTime.now());
        sessionToken.setExpiresAt(OffsetDateTime.now().plusDays(1));
        sessionTokenRepository.save(sessionToken);

        UserProfile profile = new UserProfile(user.getUsername(), user.getDisplayName(), role.name(), null);
        return new AuthLoginResponse(true, token, profile);
    }

    public UserProfile register(AuthRegisterRequest request) {
        if (request == null || request.getUsername() == null || request.getPassword() == null
                || request.getRole() == null) {
            throw new IllegalArgumentException("Missing fields");
        }
        String username = request.getUsername().trim().toLowerCase(Locale.ROOT);
        if (username.isEmpty()) {
            throw new IllegalArgumentException("Username required");
        }
        if (appUserRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }
        UserRole role = UserRole.valueOf(request.getRole().trim().toLowerCase(Locale.ROOT));
        AppUser user = new AppUser();
        user.setUsername(username);
        user.setDisplayName(request.getDisplayName() == null ? username : request.getDisplayName().trim());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole(role);
        user.setActive(true);
        user.setCreatedAt(OffsetDateTime.now());
        AppUser saved = appUserRepository.save(user);
        return new UserProfile(saved.getUsername(), saved.getDisplayName(), saved.getRole().name(), null);
    }

    public UserProfile verify(String token) {
        if (token == null || token.isBlank()) {
            return null;
        }
        SessionToken sessionToken = sessionTokenRepository.findByToken(token)
                .filter(st -> st.getExpiresAt().isAfter(OffsetDateTime.now()))
                .orElse(null);
        if (sessionToken == null) {
            return null;
        }
        AppUser user = appUserRepository.findById(sessionToken.getUserId()).orElse(null);
        if (user == null || !user.isActive()) {
            return null;
        }
        return new UserProfile(user.getUsername(), user.getDisplayName(), user.getRole().name(), null);
    }

    public void logout(String token) {
        if (token == null || token.isBlank()) {
            return;
        }
        sessionTokenRepository.deleteByToken(token);
    }
}
