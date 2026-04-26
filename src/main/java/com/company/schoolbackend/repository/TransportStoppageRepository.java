package com.company.schoolbackend.repository;

import com.company.schoolbackend.entity.TransportStoppage;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransportStoppageRepository extends JpaRepository<TransportStoppage, Long> {
    List<TransportStoppage> findByActiveTrueOrderByRouteNameAsc();
    List<TransportStoppage> findByActiveTrueOrderByStopNameAsc();
    List<TransportStoppage> findByRouteNameAndActiveTrueOrderByStopNameAsc(String routeName);
    TransportStoppage findFirstByRouteNameIgnoreCaseAndStopNameIgnoreCase(String routeName, String stopName);
    TransportStoppage findFirstByStopNameIgnoreCase(String stopName);
    List<TransportStoppage> findByRouteName(String routeName);
    void deleteByRouteName(String routeName);
}
