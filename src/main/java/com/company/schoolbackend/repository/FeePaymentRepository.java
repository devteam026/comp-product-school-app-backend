package com.company.schoolbackend.repository;

import com.company.schoolbackend.entity.FeePayment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeePaymentRepository extends JpaRepository<FeePayment, Long> {
    List<FeePayment> findByStudentIdOrderByPaymentDateDesc(String studentId);
}
