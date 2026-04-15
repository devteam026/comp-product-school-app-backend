package com.company.schoolbackend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "hostel_rooms")
public class HostelRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "hostel_id", nullable = false)
    private Long hostelId;

    @Column(name = "hostel_name", nullable = false, columnDefinition = "VARCHAR(255) DEFAULT ''")
    private String hostelName = "";

    @Column(name = "room_no", nullable = false)
    private String roomNumber;

    /**
     * Legacy column that predates room_no. Kept in sync with roomNumber so that
     * Hibernate always includes it in INSERT/UPDATE statements and MySQL's
     * NOT NULL constraint is never violated.
     */
    @Column(name = "room_number", nullable = false, columnDefinition = "VARCHAR(255) DEFAULT ''")
    private String roomNumberLegacy = "";

    @Column
    private Integer floor;

    @Column(name = "room_type")
    private String roomType;

    @Column
    private Integer capacity;

    @Column(name = "current_occupancy")
    private Integer currentOccupancy = 0;

    @Column
    private String status;

    @Column(nullable = false, columnDefinition = "TINYINT(1) DEFAULT 1")
    private boolean active = true;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getHostelId() {
        return hostelId;
    }

    public void setHostelId(Long hostelId) {
        this.hostelId = hostelId;
    }

    public String getHostelName() {
        return hostelName;
    }

    public void setHostelName(String hostelName) {
        this.hostelName = hostelName == null ? "" : hostelName;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
        // Keep the legacy room_number column in sync so inserts never violate NOT NULL
        this.roomNumberLegacy = roomNumber == null ? "" : roomNumber;
    }

    public String getRoomNumberLegacy() {
        return roomNumberLegacy;
    }

    public void setRoomNumberLegacy(String roomNumberLegacy) {
        this.roomNumberLegacy = roomNumberLegacy == null ? "" : roomNumberLegacy;
    }

    public Integer getFloor() {
        return floor;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Integer getCurrentOccupancy() {
        return currentOccupancy;
    }

    public void setCurrentOccupancy(Integer currentOccupancy) {
        this.currentOccupancy = currentOccupancy;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
