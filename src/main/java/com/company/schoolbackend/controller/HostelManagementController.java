package com.company.schoolbackend.controller;

import com.company.schoolbackend.entity.Hostel;
import com.company.schoolbackend.entity.HostelAllocation;
import com.company.schoolbackend.entity.HostelRoom;
import com.company.schoolbackend.entity.Student;
import com.company.schoolbackend.repository.HostelAllocationRepository;
import com.company.schoolbackend.repository.HostelRepository;
import com.company.schoolbackend.repository.HostelRoomRepository;
import com.company.schoolbackend.repository.StudentRepository;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/hostels/manage")
public class HostelManagementController {
    private final HostelRepository hostelRepository;
    private final HostelRoomRepository hostelRoomRepository;
    private final HostelAllocationRepository allocationRepository;
    private final StudentRepository studentRepository;

    public HostelManagementController(
            HostelRepository hostelRepository,
            HostelRoomRepository hostelRoomRepository,
            HostelAllocationRepository allocationRepository,
            StudentRepository studentRepository
    ) {
        this.hostelRepository = hostelRepository;
        this.hostelRoomRepository = hostelRoomRepository;
        this.allocationRepository = allocationRepository;
        this.studentRepository = studentRepository;
    }

    @GetMapping
    public List<Hostel> listHostels() {
        return hostelRepository.findAll();
    }

    @PostMapping
    public Hostel createHostel(@RequestBody Hostel request) {
        Hostel hostel = new Hostel();
        hostel.setName(request.getName());
        hostel.setType(request.getType());
        hostel.setCapacity(request.getCapacity());
        hostel.setWardenId(request.getWardenId());
        hostel.setContactNumber(request.getContactNumber());
        hostel.setAddress(request.getAddress());
        hostel.setSchoolId(request.getSchoolId());
        return hostelRepository.save(hostel);
    }

    @PutMapping("/{id}")
    public Hostel updateHostel(@PathVariable Long id, @RequestBody Hostel request) {
        Hostel hostel = hostelRepository.findById(id).orElseThrow();
        hostel.setName(request.getName());
        hostel.setType(request.getType());
        hostel.setCapacity(request.getCapacity());
        hostel.setWardenId(request.getWardenId());
        hostel.setContactNumber(request.getContactNumber());
        hostel.setAddress(request.getAddress());
        hostel.setSchoolId(request.getSchoolId());
        return hostelRepository.save(hostel);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteHostel(@PathVariable Long id) {
        hostelRepository.deleteById(id);
        return ResponseEntity.ok(java.util.Map.of("ok", true));
    }

    @GetMapping("/rooms")
    public List<HostelRoom> listRooms() {
        return hostelRoomRepository.findAll();
    }

    @PostMapping("/rooms")
    public HostelRoom createRoom(@RequestBody HostelRoom request) {
        HostelRoom room = new HostelRoom();
        room.setHostelId(request.getHostelId());
        room.setRoomNumber(request.getRoomNumber());
        room.setFloor(request.getFloor());
        room.setRoomType(request.getRoomType());
        room.setCapacity(request.getCapacity());
        room.setCurrentOccupancy(request.getCurrentOccupancy());
        room.setStatus(request.getStatus());
        return hostelRoomRepository.save(room);
    }

    @PutMapping("/rooms/{id}")
    public HostelRoom updateRoom(@PathVariable Long id, @RequestBody HostelRoom request) {
        HostelRoom room = hostelRoomRepository.findById(id).orElseThrow();
        room.setHostelId(request.getHostelId());
        room.setRoomNumber(request.getRoomNumber());
        room.setFloor(request.getFloor());
        room.setRoomType(request.getRoomType());
        room.setCapacity(request.getCapacity());
        room.setCurrentOccupancy(request.getCurrentOccupancy());
        room.setStatus(request.getStatus());
        return hostelRoomRepository.save(room);
    }

    @DeleteMapping("/rooms/{id}")
    public ResponseEntity<?> deleteRoom(@PathVariable Long id) {
        hostelRoomRepository.deleteById(id);
        return ResponseEntity.ok(java.util.Map.of("ok", true));
    }

    @GetMapping("/allocations")
    public List<HostelAllocation> listAllocations() {
        return allocationRepository.findAll();
    }

    @PostMapping("/allocations")
    public HostelAllocation createAllocation(@RequestBody HostelAllocation request) {
        HostelAllocation allocation = new HostelAllocation();
        allocation.setStudentId(request.getStudentId());
        allocation.setHostelId(request.getHostelId());
        allocation.setRoomId(request.getRoomId());
        allocation.setBedNumber(request.getBedNumber());
        allocation.setAllocationDate(request.getAllocationDate());
        allocation.setVacateDate(request.getVacateDate());
        allocation.setStatus(request.getStatus());
        validateCapacity(allocation.getRoomId(), null, allocation.getStatus());
        HostelAllocation saved = allocationRepository.save(allocation);
        refreshRoomOccupancy(saved.getRoomId());
        syncStudentHostel(saved);
        return saved;
    }

    @PutMapping("/allocations/{id}")
    public HostelAllocation updateAllocation(
            @PathVariable Long id,
            @RequestBody HostelAllocation request
    ) {
        HostelAllocation allocation = allocationRepository.findById(id).orElseThrow();
        Long previousRoomId = allocation.getRoomId();
        allocation.setStudentId(request.getStudentId());
        allocation.setHostelId(request.getHostelId());
        allocation.setRoomId(request.getRoomId());
        allocation.setBedNumber(request.getBedNumber());
        allocation.setAllocationDate(request.getAllocationDate());
        allocation.setVacateDate(request.getVacateDate());
        allocation.setStatus(request.getStatus());
        validateCapacity(allocation.getRoomId(), allocation.getId(), allocation.getStatus());
        HostelAllocation saved = allocationRepository.save(allocation);
        refreshRoomOccupancy(saved.getRoomId());
        syncStudentHostel(saved);
        if (previousRoomId != null && !previousRoomId.equals(saved.getRoomId())) {
            refreshRoomOccupancy(previousRoomId);
        }
        return saved;
    }

    @DeleteMapping("/allocations/{id}")
    public ResponseEntity<?> deleteAllocation(@PathVariable Long id) {
        HostelAllocation allocation = allocationRepository.findById(id).orElse(null);
        allocationRepository.deleteById(id);
        if (allocation != null) {
            refreshRoomOccupancy(allocation.getRoomId());
            clearStudentHostel(allocation.getStudentId());
        }
        return ResponseEntity.ok(java.util.Map.of("ok", true));
    }

    @PostMapping("/allocations/by-student")
    public HostelAllocation upsertAllocationForStudent(@RequestBody StudentHostelRequest request) {
        if (request == null || request.studentId == null || request.studentId.isBlank()) {
            throw new IllegalArgumentException("Student required");
        }
        if (request.hostelName == null || request.hostelName.isBlank()) {
            throw new IllegalArgumentException("Hostel name is required");
        }
        if (request.roomNumber == null || request.roomNumber.isBlank()) {
            throw new IllegalArgumentException("Room number is required");
        }
        Hostel hostel = hostelRepository.findFirstByNameIgnoreCase(request.hostelName.trim());
        if (hostel == null) {
            throw new IllegalArgumentException("Hostel not found");
        }
        HostelRoom room = hostelRoomRepository.findFirstByHostelIdAndRoomNumber(
                hostel.getId(), request.roomNumber.trim()
        );
        if (room == null) {
            throw new IllegalArgumentException("Room not found");
        }
        HostelAllocation allocation = allocationRepository
                .findFirstByStudentIdAndStatusOrderByIdDesc(request.studentId.trim(), "ACTIVE");
        if (allocation == null) {
            allocation = new HostelAllocation();
        }
        allocation.setStudentId(request.studentId.trim());
        allocation.setHostelId(hostel.getId());
        allocation.setRoomId(room.getId());
        allocation.setBedNumber(request.bedNumber);
        allocation.setAllocationDate(
                request.allocationDate != null
                        ? request.allocationDate.toString()
                        : java.time.LocalDate.now().toString()
        );
        allocation.setVacateDate(request.vacateDate != null ? request.vacateDate.toString() : null);
        allocation.setStatus("ACTIVE");
        validateCapacity(allocation.getRoomId(), allocation.getId(), allocation.getStatus());
        HostelAllocation saved = allocationRepository.save(allocation);
        refreshRoomOccupancy(saved.getRoomId());
        syncStudentHostel(saved);
        return saved;
    }

    @DeleteMapping("/allocations/by-student/{studentId}")
    public ResponseEntity<?> clearAllocationForStudent(@PathVariable String studentId) {
        if (studentId == null || studentId.isBlank()) {
            throw new IllegalArgumentException("Student required");
        }
        List<HostelAllocation> allocations = allocationRepository.findByStudentId(studentId);
        for (HostelAllocation allocation : allocations) {
            allocationRepository.deleteById(allocation.getId());
            refreshRoomOccupancy(allocation.getRoomId());
        }
        clearStudentHostel(studentId);
        return ResponseEntity.ok(java.util.Map.of("ok", true));
    }

    private void refreshRoomOccupancy(Long roomId) {
        if (roomId == null) return;
        HostelRoom room = hostelRoomRepository.findById(roomId).orElse(null);
        if (room == null) return;
        long activeCount = allocationRepository.countByRoomIdAndStatus(roomId, "ACTIVE");
        room.setCurrentOccupancy((int) activeCount);
        Integer capacity = room.getCapacity();
        if (capacity == null || capacity <= 0) {
            room.setStatus("AVAILABLE");
        } else if (activeCount >= capacity) {
            room.setStatus("FULL");
        } else {
            room.setStatus("AVAILABLE");
        }
        hostelRoomRepository.save(room);
    }

    private void validateCapacity(Long roomId, Long allocationId, String status) {
        if (roomId == null) return;
        if (!"ACTIVE".equalsIgnoreCase(status)) return;
        HostelRoom room = hostelRoomRepository.findById(roomId).orElse(null);
        if (room == null) return;
        Integer capacity = room.getCapacity();
        if (capacity == null || capacity <= 0) return;
        long activeCount = allocationId == null
                ? allocationRepository.countByRoomIdAndStatus(roomId, "ACTIVE")
                : allocationRepository.countByRoomIdAndStatusAndIdNot(roomId, "ACTIVE", allocationId);
        if (activeCount >= capacity) {
            throw new IllegalArgumentException("Room capacity reached");
        }
    }

    private void syncStudentHostel(HostelAllocation allocation) {
        if (allocation == null || allocation.getStudentId() == null) return;
        Student student = studentRepository.findById(allocation.getStudentId()).orElse(null);
        if (student == null) return;
        if (!"ACTIVE".equalsIgnoreCase(allocation.getStatus())) {
            clearStudentHostel(allocation.getStudentId());
            return;
        }
        Hostel hostel = hostelRepository.findById(allocation.getHostelId()).orElse(null);
        HostelRoom room = hostelRoomRepository.findById(allocation.getRoomId()).orElse(null);
        student.setHostelRequired(true);
        student.setHostelName(hostel == null ? null : hostel.getName());
        student.setHostelRoomNo(room == null ? null : room.getRoomNumber());
        studentRepository.save(student);
    }

    private void clearStudentHostel(String studentId) {
        Student student = studentRepository.findById(studentId).orElse(null);
        if (student == null) return;
        student.setHostelRequired(false);
        student.setHostelName(null);
        student.setHostelRoomNo(null);
        studentRepository.save(student);
    }

    static class StudentHostelRequest {
        public String studentId;
        public String hostelName;
        public String roomNumber;
        public String bedNumber;
        public java.time.LocalDate allocationDate;
        public java.time.LocalDate vacateDate;
    }
}
