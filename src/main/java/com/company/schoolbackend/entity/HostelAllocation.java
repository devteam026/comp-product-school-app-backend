package com.company.schoolbackend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "hostel_allocations")
public class HostelAllocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student_id", nullable = false, length = 36)
    private String studentId;

    @Column(name = "hostel_id", nullable = false)
    private Long hostelId;

    @Column(name = "room_id", nullable = false)
    private Long roomId;

    @Column(name = "bed_number")
    private String bedNumber;

    @Column(name = "allocation_date")
    private String allocationDate;

    @Column(name = "vacate_date")
    private String vacateDate;

    @Column
    private String status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public Long getHostelId() {
        return hostelId;
    }

    public void setHostelId(Long hostelId) {
        this.hostelId = hostelId;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public String getBedNumber() {
        return bedNumber;
    }

    public void setBedNumber(String bedNumber) {
        this.bedNumber = bedNumber;
    }

    public String getAllocationDate() {
        return allocationDate;
    }

    public void setAllocationDate(String allocationDate) {
        this.allocationDate = allocationDate;
    }

    public String getVacateDate() {
        return vacateDate;
    }

    public void setVacateDate(String vacateDate) {
        this.vacateDate = vacateDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
