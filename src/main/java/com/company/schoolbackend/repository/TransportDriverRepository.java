package com.company.schoolbackend.repository;

import com.company.schoolbackend.entity.TransportDriver;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransportDriverRepository extends JpaRepository<TransportDriver, Long> {
    List<TransportDriver> findByActiveTrueOrderByNameAsc();
}
