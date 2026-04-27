package com.company.schoolbackend.repository;

import com.company.schoolbackend.entity.TransportAssignment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransportAssignmentRepository extends JpaRepository<TransportAssignment, Long> {
    List<TransportAssignment> findByActiveTrueOrderByRouteNameAsc();
    Optional<TransportAssignment> findFirstByRouteNameIgnoreCase(String routeName);
    List<TransportAssignment> findByRouteName(String routeName);
    List<TransportAssignment> findByVehicleNo(String vehicleNo);
    List<TransportAssignment> findByDriverId(Long driverId);
    void deleteByRouteName(String routeName);
    void deleteByVehicleNo(String vehicleNo);
    void deleteByDriverId(Long driverId);
}
