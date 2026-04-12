package com.company.schoolbackend.repository;

import com.company.schoolbackend.entity.StudentDiscount;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentDiscountRepository extends JpaRepository<StudentDiscount, Long> {
    List<StudentDiscount> findByStudentId(String studentId);
}
