package com.company.schoolbackend.repository;

import com.company.schoolbackend.entity.LeaveRequest;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {
    List<LeaveRequest> findByEmployeeIdOrderByAppliedAtDesc(Long employeeId);
    List<LeaveRequest> findByRoleOrderByAppliedAtDesc(String role);
    long countByEmployeeIdAndCategoryIdAndFromDateBetweenAndStatusNot(
            Long employeeId,
            Long categoryId,
            LocalDate startDate,
            LocalDate endDate,
            String status
    );
    List<LeaveRequest> findByEmployeeIdAndCategoryIdAndFromDateLessThanEqualAndToDateGreaterThanEqualAndStatusNot(
            Long employeeId,
            Long categoryId,
            LocalDate endDate,
            LocalDate startDate,
            String status
    );
}
