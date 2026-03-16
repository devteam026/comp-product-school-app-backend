package com.company.schoolbackend.repository;

import com.company.schoolbackend.entity.EmployeeTeacherDetails;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeTeacherDetailsRepository extends JpaRepository<EmployeeTeacherDetails, Long> {
    Optional<EmployeeTeacherDetails> findByEmployeeId(Long employeeId);
}
