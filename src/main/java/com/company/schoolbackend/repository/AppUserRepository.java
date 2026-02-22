package com.company.schoolbackend.repository;

import com.company.schoolbackend.entity.AppUser;
import com.company.schoolbackend.entity.UserRole;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByUsername(String username);
    Optional<AppUser> findByUsernameAndRole(String username, UserRole role);
}
