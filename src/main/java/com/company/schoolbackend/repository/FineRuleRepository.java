package com.company.schoolbackend.repository;

import com.company.schoolbackend.entity.FineRule;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FineRuleRepository extends JpaRepository<FineRule, Long> {
    List<FineRule> findByActiveTrueOrderByDaysFromAsc();
}
