package com.company.schoolbackend.repository;

import com.company.schoolbackend.entity.LeaveCategory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeaveCategoryRepository extends JpaRepository<LeaveCategory, Long> {
    List<LeaveCategory> findByRoleAndActiveTrueOrderByNameAsc(String role);
    List<LeaveCategory> findByActiveTrueOrderByNameAsc();
}
