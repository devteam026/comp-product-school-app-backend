package com.company.schoolbackend.repository;

import com.company.schoolbackend.entity.FeePaymentDetail;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeePaymentDetailRepository extends JpaRepository<FeePaymentDetail, Long> {
    List<FeePaymentDetail> findByPaymentId(Long paymentId);
}
