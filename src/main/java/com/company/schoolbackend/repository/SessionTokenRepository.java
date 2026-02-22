package com.company.schoolbackend.repository;

import com.company.schoolbackend.entity.SessionToken;
import java.time.OffsetDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionTokenRepository extends JpaRepository<SessionToken, Long> {
    Optional<SessionToken> findByToken(String token);
    void deleteByToken(String token);
    void deleteByUserId(Long userId);
    void deleteByExpiresAtBefore(OffsetDateTime cutoff);
}
