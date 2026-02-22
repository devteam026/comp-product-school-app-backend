package com.company.schoolbackend.repository;

import com.company.schoolbackend.entity.Payment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, String> {
    List<Payment> findByStudentId(String studentId);
    List<Payment> findByStudentIdAndMonthsContains(String studentId, String month);
    List<Payment> findByMonthsContains(String month);
}
