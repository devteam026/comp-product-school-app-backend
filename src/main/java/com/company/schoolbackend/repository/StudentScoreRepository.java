package com.company.schoolbackend.repository;

import com.company.schoolbackend.entity.StudentScore;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentScoreRepository extends JpaRepository<StudentScore, Long> {
    List<StudentScore> findByStudentIdIn(List<String> studentIds);
}
