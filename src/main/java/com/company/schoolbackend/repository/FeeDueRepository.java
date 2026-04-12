package com.company.schoolbackend.repository;

import com.company.schoolbackend.entity.FeeDue;
import com.company.schoolbackend.entity.FeeDueStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeeDueRepository extends JpaRepository<FeeDue, Long> {
    List<FeeDue> findByStudentIdOrderByDueDateAsc(String studentId);
    List<FeeDue> findByStudentIdAndStatusOrderByDueDateAsc(String studentId, FeeDueStatus status);
    List<FeeDue> findByStatusOrderByDueDateAsc(FeeDueStatus status);
    List<FeeDue> findByStudentIdAndFeeTypeIdAndDueDate(String studentId, Long feeTypeId, String dueDate);
}
