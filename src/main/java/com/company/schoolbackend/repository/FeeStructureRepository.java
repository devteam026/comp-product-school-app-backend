package com.company.schoolbackend.repository;

import com.company.schoolbackend.entity.FeeStructure;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeeStructureRepository extends JpaRepository<FeeStructure, Long> {
    List<FeeStructure> findByClassCodeAndActiveTrueOrderByFeeTypeIdAsc(String classCode);
    List<FeeStructure> findByActiveTrueOrderByClassCodeAsc();
}
