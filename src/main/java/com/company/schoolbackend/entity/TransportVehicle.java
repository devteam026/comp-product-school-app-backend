package com.company.schoolbackend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "transport_vehicles")
public class TransportVehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "vehicle_no", nullable = false)
    private String vehicleNo;

    @Column(name = "vehicle_type")
    private String vehicleType;

    @Column(name = "capacity")
    private Integer capacity;

    @Column(name = "insurance_number")
    private String insuranceNumber;

    @Column(name = "insurance_expiry_date")
    private String insuranceExpiryDate;

    @Column(name = "fitness_expiry_date")
    private String fitnessExpiryDate;

    @Column(name = "pollution_expiry_date")
    private String pollutionExpiryDate;

    @Column(name = "permit_number")
    private String permitNumber;

    @Column(name = "permit_expiry_date")
    private String permitExpiryDate;

    @Column(name = "insurance_doc_key")
    private String insuranceDocKey;

    @Column(name = "fitness_doc_key")
    private String fitnessDocKey;

    @Column(name = "pollution_doc_key")
    private String pollutionDocKey;

    @Column(name = "permit_doc_key")
    private String permitDocKey;

    @Column(nullable = false)
    private boolean active = true;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public String getInsuranceNumber() {
        return insuranceNumber;
    }

    public void setInsuranceNumber(String insuranceNumber) {
        this.insuranceNumber = insuranceNumber;
    }

    public String getInsuranceExpiryDate() {
        return insuranceExpiryDate;
    }

    public void setInsuranceExpiryDate(String insuranceExpiryDate) {
        this.insuranceExpiryDate = insuranceExpiryDate;
    }

    public String getFitnessExpiryDate() {
        return fitnessExpiryDate;
    }

    public void setFitnessExpiryDate(String fitnessExpiryDate) {
        this.fitnessExpiryDate = fitnessExpiryDate;
    }

    public String getPollutionExpiryDate() {
        return pollutionExpiryDate;
    }

    public void setPollutionExpiryDate(String pollutionExpiryDate) {
        this.pollutionExpiryDate = pollutionExpiryDate;
    }

    public String getPermitNumber() {
        return permitNumber;
    }

    public void setPermitNumber(String permitNumber) {
        this.permitNumber = permitNumber;
    }

    public String getPermitExpiryDate() {
        return permitExpiryDate;
    }

    public void setPermitExpiryDate(String permitExpiryDate) {
        this.permitExpiryDate = permitExpiryDate;
    }

    public String getInsuranceDocKey() {
        return insuranceDocKey;
    }

    public void setInsuranceDocKey(String insuranceDocKey) {
        this.insuranceDocKey = insuranceDocKey;
    }

    public String getFitnessDocKey() {
        return fitnessDocKey;
    }

    public void setFitnessDocKey(String fitnessDocKey) {
        this.fitnessDocKey = fitnessDocKey;
    }

    public String getPollutionDocKey() {
        return pollutionDocKey;
    }

    public void setPollutionDocKey(String pollutionDocKey) {
        this.pollutionDocKey = pollutionDocKey;
    }

    public String getPermitDocKey() {
        return permitDocKey;
    }

    public void setPermitDocKey(String permitDocKey) {
        this.permitDocKey = permitDocKey;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
