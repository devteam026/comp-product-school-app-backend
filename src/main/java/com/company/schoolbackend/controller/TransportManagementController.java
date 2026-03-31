package com.company.schoolbackend.controller;

import com.company.schoolbackend.dto.TransportSummaryRow;
import com.company.schoolbackend.dto.PhotoUploadResponse;
import com.company.schoolbackend.dto.UserProfile;
import com.company.schoolbackend.entity.Student;
import com.company.schoolbackend.entity.StudentStatus;
import com.company.schoolbackend.entity.TransportAssignment;
import com.company.schoolbackend.entity.TransportDriver;
import com.company.schoolbackend.entity.TransportRoute;
import com.company.schoolbackend.entity.TransportStoppage;
import com.company.schoolbackend.entity.TransportVehicle;
import com.company.schoolbackend.repository.StudentRepository;
import com.company.schoolbackend.repository.TransportAssignmentRepository;
import com.company.schoolbackend.repository.TransportDriverRepository;
import com.company.schoolbackend.repository.TransportRouteRepository;
import com.company.schoolbackend.repository.TransportStoppageRepository;
import com.company.schoolbackend.repository.TransportVehicleRepository;
import com.company.schoolbackend.service.R2Service;
import jakarta.servlet.http.HttpServletRequest;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transport")
public class TransportManagementController {
    private final TransportRouteRepository routeRepository;
    private final TransportVehicleRepository vehicleRepository;
    private final TransportDriverRepository driverRepository;
    private final TransportStoppageRepository stoppageRepository;
    private final TransportAssignmentRepository assignmentRepository;
    private final StudentRepository studentRepository;
    private final R2Service r2Service;

    public TransportManagementController(
            TransportRouteRepository routeRepository,
            TransportVehicleRepository vehicleRepository,
            TransportDriverRepository driverRepository,
            TransportStoppageRepository stoppageRepository,
            TransportAssignmentRepository assignmentRepository,
            StudentRepository studentRepository,
            R2Service r2Service
    ) {
        this.routeRepository = routeRepository;
        this.vehicleRepository = vehicleRepository;
        this.driverRepository = driverRepository;
        this.stoppageRepository = stoppageRepository;
        this.assignmentRepository = assignmentRepository;
        this.studentRepository = studentRepository;
        this.r2Service = r2Service;
    }

    @GetMapping("/routes")
    public List<TransportRoute> routes() {
        return routeRepository.findAll();
    }

    @PostMapping("/routes")
    public TransportRoute createRoute(@RequestBody TransportRoute request) {
        TransportRoute route = new TransportRoute();
        route.setName(request.getName());
        route.setActive(request.isActive());
        return routeRepository.save(route);
    }

    @PutMapping("/routes/{id}")
    public TransportRoute updateRoute(@PathVariable Long id, @RequestBody TransportRoute request) {
        TransportRoute route = routeRepository.findById(id).orElseThrow();
        route.setName(request.getName());
        route.setActive(request.isActive());
        return routeRepository.save(route);
    }

    @DeleteMapping("/routes/{id}")
    public ResponseEntity<?> deleteRoute(@PathVariable Long id) {
        routeRepository.deleteById(id);
        return ResponseEntity.ok(java.util.Map.of("ok", true));
    }

    @GetMapping("/vehicles")
    public List<TransportVehicle> vehicles() {
        return vehicleRepository.findAll();
    }

    @PostMapping("/vehicles")
    public TransportVehicle createVehicle(@RequestBody TransportVehicle request) {
        TransportVehicle vehicle = new TransportVehicle();
        vehicle.setVehicleNo(request.getVehicleNo());
        vehicle.setVehicleType(request.getVehicleType());
        vehicle.setCapacity(request.getCapacity());
        vehicle.setInsuranceNumber(request.getInsuranceNumber());
        vehicle.setInsuranceExpiryDate(request.getInsuranceExpiryDate());
        vehicle.setFitnessExpiryDate(request.getFitnessExpiryDate());
        vehicle.setPollutionExpiryDate(request.getPollutionExpiryDate());
        vehicle.setPermitNumber(request.getPermitNumber());
        vehicle.setPermitExpiryDate(request.getPermitExpiryDate());
        vehicle.setInsuranceDocKey(request.getInsuranceDocKey());
        vehicle.setFitnessDocKey(request.getFitnessDocKey());
        vehicle.setPollutionDocKey(request.getPollutionDocKey());
        vehicle.setPermitDocKey(request.getPermitDocKey());
        vehicle.setActive(request.isActive());
        return vehicleRepository.save(vehicle);
    }

    @PutMapping("/vehicles/{id}")
    public TransportVehicle updateVehicle(@PathVariable Long id, @RequestBody TransportVehicle request) {
        TransportVehicle vehicle = vehicleRepository.findById(id).orElseThrow();
        vehicle.setVehicleNo(request.getVehicleNo());
        vehicle.setVehicleType(request.getVehicleType());
        vehicle.setCapacity(request.getCapacity());
        vehicle.setInsuranceNumber(request.getInsuranceNumber());
        vehicle.setInsuranceExpiryDate(request.getInsuranceExpiryDate());
        vehicle.setFitnessExpiryDate(request.getFitnessExpiryDate());
        vehicle.setPollutionExpiryDate(request.getPollutionExpiryDate());
        vehicle.setPermitNumber(request.getPermitNumber());
        vehicle.setPermitExpiryDate(request.getPermitExpiryDate());
        vehicle.setInsuranceDocKey(request.getInsuranceDocKey());
        vehicle.setFitnessDocKey(request.getFitnessDocKey());
        vehicle.setPollutionDocKey(request.getPollutionDocKey());
        vehicle.setPermitDocKey(request.getPermitDocKey());
        vehicle.setActive(request.isActive());
        return vehicleRepository.save(vehicle);
    }

    @DeleteMapping("/vehicles/{id}")
    public ResponseEntity<?> deleteVehicle(@PathVariable Long id) {
        vehicleRepository.deleteById(id);
        return ResponseEntity.ok(java.util.Map.of("ok", true));
    }

    @PostMapping("/vehicles/upload")
    public ResponseEntity<PhotoUploadResponse> createVehicleUpload(
            @RequestParam("contentType") String contentType,
            @RequestParam("fileName") String fileName,
            @RequestParam("sizeBytes") long sizeBytes,
            @RequestParam("type") String type,
            HttpServletRequest request
    ) {
        if (!isAdmin(request)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        if (sizeBytes > (long) r2Service.getMaxUploadKb() * 1024L) {
            return new ResponseEntity<>(HttpStatus.PAYLOAD_TOO_LARGE);
        }
        String objectKey = r2Service.generateObjectKey(fileName, "vehicle_docs/" + type + "/");
        var presigned = r2Service.createUploadUrl(contentType, fileName, objectKey);
        PhotoUploadResponse response = new PhotoUploadResponse(
                presigned.url().toString(),
                objectKey,
                OffsetDateTime.now().plusMinutes(10).toString()
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/vehicles/{id}/file-url")
    public ResponseEntity<Map<String, String>> getVehicleFileUrl(
            @PathVariable Long id,
            @RequestParam("type") String type,
            HttpServletRequest request
    ) {
        if (!isAdmin(request)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        TransportVehicle vehicle = vehicleRepository.findById(id).orElse(null);
        if (vehicle == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        String key = switch (type) {
            case "insurance" -> vehicle.getInsuranceDocKey();
            case "fitness" -> vehicle.getFitnessDocKey();
            case "pollution" -> vehicle.getPollutionDocKey();
            case "permit" -> vehicle.getPermitDocKey();
            default -> null;
        };
        if (key == null || key.isBlank()) {
            return ResponseEntity.ok(Map.of("url", ""));
        }
        var presigned = r2Service.createDownloadUrl(key);
        return ResponseEntity.ok(Map.of("url", presigned.url().toString()));
    }

    @GetMapping("/drivers")
    public List<TransportDriver> drivers() {
        return driverRepository.findAll();
    }

    @PostMapping("/drivers")
    public TransportDriver createDriver(@RequestBody TransportDriver request) {
        TransportDriver driver = new TransportDriver();
        driver.setName(request.getName());
        driver.setPhone(request.getPhone());
        driver.setAlternatePhone(request.getAlternatePhone());
        driver.setBloodGroup(request.getBloodGroup());
        driver.setLicenseNo(request.getLicenseNo());
        driver.setActive(request.isActive());
        return driverRepository.save(driver);
    }

    @PutMapping("/drivers/{id}")
    public TransportDriver updateDriver(@PathVariable Long id, @RequestBody TransportDriver request) {
        TransportDriver driver = driverRepository.findById(id).orElseThrow();
        driver.setName(request.getName());
        driver.setPhone(request.getPhone());
        driver.setAlternatePhone(request.getAlternatePhone());
        driver.setBloodGroup(request.getBloodGroup());
        driver.setLicenseNo(request.getLicenseNo());
        driver.setActive(request.isActive());
        return driverRepository.save(driver);
    }

    @DeleteMapping("/drivers/{id}")
    public ResponseEntity<?> deleteDriver(@PathVariable Long id) {
        driverRepository.deleteById(id);
        return ResponseEntity.ok(java.util.Map.of("ok", true));
    }

    @GetMapping("/stoppages")
    public List<TransportStoppage> stoppages() {
        return stoppageRepository.findAll();
    }

    @PostMapping("/stoppages")
    public TransportStoppage createStoppage(@RequestBody TransportStoppage request) {
        TransportStoppage stoppage = new TransportStoppage();
        stoppage.setRouteName(request.getRouteName());
        stoppage.setStopName(request.getStopName());
        stoppage.setCheckInTime(request.getCheckInTime());
        stoppage.setCheckOutTime(request.getCheckOutTime());
        stoppage.setFeeAmount(request.getFeeAmount());
        stoppage.setDistanceKm(request.getDistanceKm());
        stoppage.setActive(request.isActive());
        return stoppageRepository.save(stoppage);
    }

    @PutMapping("/stoppages/{id}")
    public TransportStoppage updateStoppage(@PathVariable Long id, @RequestBody TransportStoppage request) {
        TransportStoppage stoppage = stoppageRepository.findById(id).orElseThrow();
        stoppage.setRouteName(request.getRouteName());
        stoppage.setStopName(request.getStopName());
        stoppage.setCheckInTime(request.getCheckInTime());
        stoppage.setCheckOutTime(request.getCheckOutTime());
        stoppage.setFeeAmount(request.getFeeAmount());
        stoppage.setDistanceKm(request.getDistanceKm());
        stoppage.setActive(request.isActive());
        return stoppageRepository.save(stoppage);
    }

    @DeleteMapping("/stoppages/{id}")
    public ResponseEntity<?> deleteStoppage(@PathVariable Long id) {
        stoppageRepository.deleteById(id);
        return ResponseEntity.ok(java.util.Map.of("ok", true));
    }

    @GetMapping("/assignments")
    public List<TransportAssignment> assignments() {
        return assignmentRepository.findAll();
    }

    @GetMapping("/assignments/{id}/students")
    public List<Map<String, Object>> assignmentStudents(@PathVariable Long id) {
        TransportAssignment assignment = assignmentRepository.findById(id).orElseThrow();
        List<String> routeStops = stoppageRepository
                .findByRouteNameAndActiveTrueOrderByStopNameAsc(assignment.getRouteName())
                .stream()
                .map(TransportStoppage::getStopName)
                .filter(name -> name != null && !name.isBlank())
                .map(String::trim)
                .toList();
        List<Student> students = studentRepository
                .findByTransportRouteAndTransportVehicleNoAndTransportRequiredTrueAndStatus(
                        assignment.getRouteName(),
                        assignment.getVehicleNo(),
                        StudentStatus.Active
                );
        if (!routeStops.isEmpty()) {
            students = students.stream()
                    .filter(student -> student.getTransportStopName() != null)
                    .filter(student -> routeStops.contains(student.getTransportStopName().trim()))
                    .toList();
        } else {
            students = students.stream()
                    .filter(student -> student.getTransportStopName() != null && !student.getTransportStopName().isBlank())
                    .toList();
        }
        List<Map<String, Object>> list = new ArrayList<>();
        for (Student student : students) {
            list.add(Map.of(
                    "id", student.getId(),
                    "name", student.getName(),
                    "classCode", student.getClassCode(),
                    "registerNo", student.getRegisterNo() == null ? "" : student.getRegisterNo(),
                    "stopName", student.getTransportStopName() == null ? "" : student.getTransportStopName()
            ));
        }
        return list;
    }

    @GetMapping("/summary")
    public List<TransportSummaryRow> summary() {
        List<TransportAssignment> assignments = assignmentRepository.findAll();
        List<TransportSummaryRow> rows = new ArrayList<>();
        for (TransportAssignment assignment : assignments) {
            TransportVehicle vehicle = vehicleRepository.findAll().stream()
                    .filter(v -> assignment.getVehicleNo().equalsIgnoreCase(v.getVehicleNo()))
                    .findFirst()
                    .orElse(null);
            TransportDriver driver = driverRepository.findById(assignment.getDriverId()).orElse(null);
            long studentCount = studentRepository
                    .countByTransportRouteAndTransportVehicleNoAndTransportRequiredTrueAndStatus(
                            assignment.getRouteName(),
                            assignment.getVehicleNo(),
                            StudentStatus.Active
                    );
            Integer capacity = vehicle == null ? null : vehicle.getCapacity();
            long freeSeats = capacity == null ? 0 : Math.max(0, capacity - studentCount);
            rows.add(new TransportSummaryRow(
                    assignment.getId(),
                    assignment.getRouteName(),
                    assignment.getVehicleNo(),
                    vehicle == null ? null : vehicle.getVehicleType(),
                    capacity,
                    driver == null ? "-" : driver.getName(),
                    driver == null ? "-" : driver.getPhone(),
                    studentCount,
                    freeSeats,
                    assignment.isActive()
            ));
        }
        return rows;
    }

    @PostMapping("/assignments")
    public TransportAssignment createAssignment(@RequestBody TransportAssignment request) {
        TransportAssignment assignment = new TransportAssignment();
        assignment.setRouteName(request.getRouteName());
        assignment.setVehicleNo(request.getVehicleNo());
        assignment.setDriverId(request.getDriverId());
        assignment.setActive(request.isActive());
        return assignmentRepository.save(assignment);
    }

    @PutMapping("/assignments/{id}")
    public TransportAssignment updateAssignment(@PathVariable Long id, @RequestBody TransportAssignment request) {
        TransportAssignment assignment = assignmentRepository.findById(id).orElseThrow();
        assignment.setRouteName(request.getRouteName());
        assignment.setVehicleNo(request.getVehicleNo());
        assignment.setDriverId(request.getDriverId());
        assignment.setActive(request.isActive());
        return assignmentRepository.save(assignment);
    }

    @DeleteMapping("/assignments/{id}")
    public ResponseEntity<?> deleteAssignment(@PathVariable Long id) {
        assignmentRepository.deleteById(id);
        return ResponseEntity.ok(java.util.Map.of("ok", true));
    }

    private boolean isAdmin(HttpServletRequest request) {
        Object profileObj = request.getAttribute("userProfile");
        if (profileObj instanceof UserProfile profile) {
            return "admin".equalsIgnoreCase(profile.getRole());
        }
        return false;
    }
}
