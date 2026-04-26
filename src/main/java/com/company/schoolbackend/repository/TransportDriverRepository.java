package com.company.schoolbackend.repository;

import com.company.schoolbackend.entity.TransportDriver;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransportDriverRepository extends JpaRepository<TransportDriver, Long> {
    List<TransportDriver> findByActiveTrueOrderByNameAsc();
    Optional<TransportDriver> findFirstByPhone(String phone);
    Optional<TransportDriver> findFirstByLicenseNoIgnoreCase(String licenseNo);
}
