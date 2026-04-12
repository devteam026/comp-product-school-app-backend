package com.company.schoolbackend.dto;

public class TransportSummaryRow {
    private Long assignmentId;
    private String routeName;
    private String vehicleNo;
    private String vehicleType;
    private Integer capacity;
    private String driverName;
    private String driverPhone;
    private long studentCount;
    private long freeSeats;
    private boolean active;

    public TransportSummaryRow(
            Long assignmentId,
            String routeName,
            String vehicleNo,
            String vehicleType,
            Integer capacity,
            String driverName,
            String driverPhone,
            long studentCount,
            long freeSeats,
            boolean active
    ) {
        this.assignmentId = assignmentId;
        this.routeName = routeName;
        this.vehicleNo = vehicleNo;
        this.vehicleType = vehicleType;
        this.capacity = capacity;
        this.driverName = driverName;
        this.driverPhone = driverPhone;
        this.studentCount = studentCount;
        this.freeSeats = freeSeats;
        this.active = active;
    }

    public Long getAssignmentId() {
        return assignmentId;
    }

    public String getRouteName() {
        return routeName;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public String getDriverName() {
        return driverName;
    }

    public String getDriverPhone() {
        return driverPhone;
    }

    public long getStudentCount() {
        return studentCount;
    }

    public long getFreeSeats() {
        return freeSeats;
    }

    public boolean isActive() {
        return active;
    }
}
