package com.company.schoolbackend.repository;

import com.company.schoolbackend.entity.FeeReceipt;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeeReceiptRepository extends JpaRepository<FeeReceipt, Long> {
    List<FeeReceipt> findByPaymentId(Long paymentId);
}
