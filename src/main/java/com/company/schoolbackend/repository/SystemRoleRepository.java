package com.company.schoolbackend.repository;

import com.company.schoolbackend.entity.SystemRole;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SystemRoleRepository extends JpaRepository<SystemRole, Long> {
    Optional<SystemRole> findByNameIgnoreCase(String name);
    boolean existsByNameIgnoreCase(String name);
}
