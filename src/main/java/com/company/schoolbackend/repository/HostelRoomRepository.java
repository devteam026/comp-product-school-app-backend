package com.company.schoolbackend.repository;

import com.company.schoolbackend.entity.HostelRoom;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HostelRoomRepository extends JpaRepository<HostelRoom, Long> {
    List<HostelRoom> findAllByOrderByRoomNumberAsc();
    List<HostelRoom> findByHostelIdOrderByRoomNumberAsc(Long hostelId);
    HostelRoom findFirstByHostelIdAndRoomNumber(Long hostelId, String roomNumber);
}
