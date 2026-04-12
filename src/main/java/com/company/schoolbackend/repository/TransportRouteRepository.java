package com.company.schoolbackend.repository;

import com.company.schoolbackend.entity.TransportRoute;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransportRouteRepository extends JpaRepository<TransportRoute, Long> {
    List<TransportRoute> findByActiveTrueOrderByNameAsc();
}
