package com.company.schoolbackend.repository;

import com.company.schoolbackend.entity.TransportStoppage;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransportStoppageRepository extends JpaRepository<TransportStoppage, Long> {
    List<TransportStoppage> findByActiveTrueOrderByRouteNameAsc();
}
