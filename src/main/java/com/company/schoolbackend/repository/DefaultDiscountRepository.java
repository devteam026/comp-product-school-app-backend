package com.company.schoolbackend.repository;

import com.company.schoolbackend.entity.DefaultDiscount;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DefaultDiscountRepository extends JpaRepository<DefaultDiscount, Long> {
    List<DefaultDiscount> findByActiveTrueOrderByNameAsc();
}
