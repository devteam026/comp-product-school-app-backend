package com.company.schoolbackend.repository;

import com.company.schoolbackend.entity.StudentHistory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentHistoryRepository extends JpaRepository<StudentHistory, Long> {
    List<StudentHistory> findByStudentIdOrderByCreatedAtDesc(String studentId);
}
