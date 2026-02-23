package com.company.schoolbackend.repository;

import com.company.schoolbackend.entity.SchoolClass;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchoolClassRepository extends JpaRepository<SchoolClass, Long> {
    List<SchoolClass> findByActiveTrueOrderByClassCodeAsc();
}
