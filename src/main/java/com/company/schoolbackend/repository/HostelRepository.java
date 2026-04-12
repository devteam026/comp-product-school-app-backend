package com.company.schoolbackend.repository;

import com.company.schoolbackend.entity.Hostel;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HostelRepository extends JpaRepository<Hostel, Long> {
    List<Hostel> findAllByOrderByNameAsc();
    Hostel findFirstByNameIgnoreCase(String name);
}
