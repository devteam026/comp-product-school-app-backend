package com.company.schoolbackend.repository;

import com.company.schoolbackend.entity.FeeTypeEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeeTypeRepository extends JpaRepository<FeeTypeEntity, Long> {
    List<FeeTypeEntity> findByActiveTrueOrderByNameAsc();
}
