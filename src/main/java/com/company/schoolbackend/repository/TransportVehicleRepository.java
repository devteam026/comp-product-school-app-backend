package com.company.schoolbackend.repository;

import com.company.schoolbackend.entity.TransportVehicle;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransportVehicleRepository extends JpaRepository<TransportVehicle, Long> {
    List<TransportVehicle> findByActiveTrueOrderByVehicleNoAsc();
}
