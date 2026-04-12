package com.company.schoolbackend.repository;

import com.company.schoolbackend.entity.HostelAllocation;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HostelAllocationRepository extends JpaRepository<HostelAllocation, Long> {
    List<HostelAllocation> findByHostelId(Long hostelId);
    List<HostelAllocation> findByRoomId(Long roomId);
    List<HostelAllocation> findByStudentId(String studentId);
    HostelAllocation findFirstByStudentIdAndStatusOrderByIdDesc(String studentId, String status);
    long countByRoomIdAndStatusAndIdNot(Long roomId, String status, Long id);
    long countByRoomIdAndStatus(Long roomId, String status);
}
