package com.company.schoolbackend.repository;

import com.company.schoolbackend.entity.TransportAssignment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransportAssignmentRepository extends JpaRepository<TransportAssignment, Long> {
    List<TransportAssignment> findByActiveTrueOrderByRouteNameAsc();
}
