package com.company.schoolbackend.controller;

import com.company.schoolbackend.dto.TransportSummaryRow;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.util.Optional;

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
    public ResponseEntity<?> createRoute(@RequestBody TransportRoute request) {
        Optional<TransportRoute> existing = routeRepository.findFirstByNameIgnoreCase(request.getName());
        if (existing.isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("error", "A route with this name already exists."));
        }
        TransportRoute route = new TransportRoute();
        route.setName(request.getName());
        route.setActive(request.isActive());
        return ResponseEntity.ok(routeRepository.save(route));
    }

    @PutMapping("/routes/{id}")
    public ResponseEntity<?> updateRoute(@PathVariable Long id, @RequestBody TransportRoute request) {
        Optional<TransportRoute> existing = routeRepository.findFirstByNameIgnoreCase(request.getName());
        if (existing.isPresent() && !existing.get().getId().equals(id)) {
            return ResponseEntity.badRequest().body(Map.of("error", "A route with this name already exists."));
        }
        TransportRoute route = routeRepository.findById(id).orElseThrow();
        route.setName(request.getName());
        route.setActive(request.isActive());
        return ResponseEntity.ok(routeRepository.save(route));
    }

    @DeleteMapping("/routes/{id}")
    @Transactional
    public ResponseEntity<?> deleteRoute(@PathVariable Long id) {
        TransportRoute route = routeRepository.findById(id).orElseThrow();
        stoppageRepository.deleteByRouteName(route.getName());
        assignmentRepository.deleteByRouteName(route.getName());
        routeRepository.deleteById(id);
        return ResponseEntity.ok(Map.of("ok", true));
    }

    @GetMapping("/vehicles")
    public List<TransportVehicle> vehicles() {
        return vehicleRepository.findAll();
    }

    @PostMapping("/vehicles")
    public ResponseEntity<?> createVehicle(@RequestBody TransportVehicle request) {
        Optional<TransportVehicle> existing = vehicleRepository.findFirstByVehicleNoIgnoreCase(request.getVehicleNo());
        if (existing.isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("error", "A vehicle with this number already exists."));
        }
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
        return ResponseEntity.ok(vehicleRepository.save(vehicle));
    }

    @PutMapping("/vehicles/{id}")
    public ResponseEntity<?> updateVehicle(@PathVariable Long id, @RequestBody TransportVehicle request) {
        Optional<TransportVehicle> existing = vehicleRepository.findFirstByVehicleNoIgnoreCase(request.getVehicleNo());
        if (existing.isPresent() && !existing.get().getId().equals(id)) {
            return ResponseEntity.badRequest().body(Map.of("error", "A vehicle with this number already exists."));
        }
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
        return ResponseEntity.ok(vehicleRepository.save(vehicle));
    }

    @DeleteMapping("/vehicles/{id}")
    @Transactional
    public ResponseEntity<?> deleteVehicle(@PathVariable Long id) {
        TransportVehicle vehicle = vehicleRepository.findById(id).orElseThrow();
        assignmentRepository.deleteByVehicleNo(vehicle.getVehicleNo());
        vehicleRepository.deleteById(id);
        return ResponseEntity.ok(Map.of("ok", true));
    }

    @PostMapping(value = "/vehicles/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadVehicleDoc(
            @RequestParam("file") MultipartFile file,
            @RequestParam("type") String type,
            HttpServletRequest request
    ) {
        if (!isAdmin(request)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        if (file.getSize() > (long) r2Service.getMaxUploadKb() * 1024L * 10) {
            return ResponseEntity.badRequest().body(Map.of("error", "File too large. Max 10 MB."));
        }
        try {
            String objectKey = r2Service.generateObjectKey(file.getOriginalFilename(), "vehicle_docs/" + type + "/");
            String contentType = file.getContentType() != null ? file.getContentType() : "application/octet-stream";
            r2Service.uploadFile(objectKey, file.getBytes(), contentType);
            return ResponseEntity.ok(Map.of("objectKey", objectKey));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Upload failed: " + e.getMessage()));
        }
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
    public ResponseEntity<?> createDriver(@RequestBody TransportDriver request) {
        Optional<TransportDriver> byPhone = driverRepository.findFirstByPhone(request.getPhone());
        if (byPhone.isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("error", "A driver with this phone number already exists."));
        }
        if (request.getLicenseNo() != null && !request.getLicenseNo().isBlank()) {
            Optional<TransportDriver> byLicense = driverRepository.findFirstByLicenseNoIgnoreCase(request.getLicenseNo());
            if (byLicense.isPresent()) {
                return ResponseEntity.badRequest().body(Map.of("error", "A driver with this license number already exists."));
            }
        }
        TransportDriver driver = new TransportDriver();
        driver.setName(request.getName());
        driver.setPhone(request.getPhone());
        driver.setAlternatePhone(request.getAlternatePhone());
        driver.setBloodGroup(request.getBloodGroup());
        driver.setLicenseNo(request.getLicenseNo());
        driver.setActive(request.isActive());
        return ResponseEntity.ok(driverRepository.save(driver));
    }

    @PutMapping("/drivers/{id}")
    public ResponseEntity<?> updateDriver(@PathVariable Long id, @RequestBody TransportDriver request) {
        Optional<TransportDriver> byPhone = driverRepository.findFirstByPhone(request.getPhone());
        if (byPhone.isPresent() && !byPhone.get().getId().equals(id)) {
            return ResponseEntity.badRequest().body(Map.of("error", "A driver with this phone number already exists."));
        }
        if (request.getLicenseNo() != null && !request.getLicenseNo().isBlank()) {
            Optional<TransportDriver> byLicense = driverRepository.findFirstByLicenseNoIgnoreCase(request.getLicenseNo());
            if (byLicense.isPresent() && !byLicense.get().getId().equals(id)) {
                return ResponseEntity.badRequest().body(Map.of("error", "A driver with this license number already exists."));
            }
        }
        TransportDriver driver = driverRepository.findById(id).orElseThrow();
        driver.setName(request.getName());
        driver.setPhone(request.getPhone());
        driver.setAlternatePhone(request.getAlternatePhone());
        driver.setBloodGroup(request.getBloodGroup());
        driver.setLicenseNo(request.getLicenseNo());
        driver.setActive(request.isActive());
        return ResponseEntity.ok(driverRepository.save(driver));
    }

    @DeleteMapping("/drivers/{id}")
    @Transactional
    public ResponseEntity<?> deleteDriver(@PathVariable Long id) {
        assignmentRepository.deleteByDriverId(id);
        driverRepository.deleteById(id);
        return ResponseEntity.ok(Map.of("ok", true));
    }

    @GetMapping("/stoppages")
    public List<TransportStoppage> stoppages() {
        return stoppageRepository.findAll();
    }

    @PostMapping("/stoppages")
    public ResponseEntity<?> createStoppage(@RequestBody TransportStoppage request) {
        TransportStoppage existing = stoppageRepository.findFirstByRouteNameIgnoreCaseAndStopNameIgnoreCase(
                request.getRouteName(), request.getStopName());
        if (existing != null) {
            return ResponseEntity.badRequest().body(Map.of("error", "This stop already exists on the selected route."));
        }
        TransportStoppage stoppage = new TransportStoppage();
        stoppage.setRouteName(request.getRouteName());
        stoppage.setStopName(request.getStopName());
        stoppage.setCheckInTime(request.getCheckInTime());
        stoppage.setCheckOutTime(request.getCheckOutTime());
        stoppage.setFeeAmount(request.getFeeAmount());
        stoppage.setDistanceKm(request.getDistanceKm());
        stoppage.setActive(request.isActive());
        return ResponseEntity.ok(stoppageRepository.save(stoppage));
    }

    @PutMapping("/stoppages/{id}")
    public ResponseEntity<?> updateStoppage(@PathVariable Long id, @RequestBody TransportStoppage request) {
        TransportStoppage existing = stoppageRepository.findFirstByRouteNameIgnoreCaseAndStopNameIgnoreCase(
                request.getRouteName(), request.getStopName());
        if (existing != null && !existing.getId().equals(id)) {
            return ResponseEntity.badRequest().body(Map.of("error", "This stop already exists on the selected route."));
        }
        TransportStoppage stoppage = stoppageRepository.findById(id).orElseThrow();
        stoppage.setRouteName(request.getRouteName());
        stoppage.setStopName(request.getStopName());
        stoppage.setCheckInTime(request.getCheckInTime());
        stoppage.setCheckOutTime(request.getCheckOutTime());
        stoppage.setFeeAmount(request.getFeeAmount());
        stoppage.setDistanceKm(request.getDistanceKm());
        stoppage.setActive(request.isActive());
        return ResponseEntity.ok(stoppageRepository.save(stoppage));
    }

    @DeleteMapping("/stoppages/{id}")
    public ResponseEntity<?> deleteStoppage(@PathVariable Long id) {
        stoppageRepository.deleteById(id);
        return ResponseEntity.ok(Map.of("ok", true));
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
        if (routeStops.isEmpty()) {
            return List.of();
        }
        List<Student> students = studentRepository
                .findByTransportStopNameInAndTransportRequiredTrueAndStatus(routeStops, StudentStatus.Active);
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
            List<String> routeStops = stoppageRepository
                    .findByRouteName(assignment.getRouteName())
                    .stream()
                    .map(TransportStoppage::getStopName)
                    .filter(name -> name != null && !name.isBlank())
                    .map(String::trim)
                    .toList();
            long studentCount = routeStops.isEmpty() ? 0 :
                    studentRepository.countByTransportStopNameInAndTransportRequiredTrueAndStatus(
                            routeStops, StudentStatus.Active);
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
    public ResponseEntity<?> createAssignment(@RequestBody TransportAssignment request) {
        Optional<TransportAssignment> existing = assignmentRepository.findFirstByRouteNameIgnoreCase(request.getRouteName());
        if (existing.isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("error", "A vehicle is already assigned to this route."));
        }
        TransportAssignment assignment = new TransportAssignment();
        assignment.setRouteName(request.getRouteName());
        assignment.setVehicleNo(request.getVehicleNo());
        assignment.setDriverId(request.getDriverId());
        assignment.setActive(request.isActive());
        return ResponseEntity.ok(assignmentRepository.save(assignment));
    }

    @PutMapping("/assignments/{id}")
    public ResponseEntity<?> updateAssignment(@PathVariable Long id, @RequestBody TransportAssignment request) {
        Optional<TransportAssignment> existing = assignmentRepository.findFirstByRouteNameIgnoreCase(request.getRouteName());
        if (existing.isPresent() && !existing.get().getId().equals(id)) {
            return ResponseEntity.badRequest().body(Map.of("error", "A vehicle is already assigned to this route."));
        }
        TransportAssignment assignment = assignmentRepository.findById(id).orElseThrow();
        assignment.setRouteName(request.getRouteName());
        assignment.setVehicleNo(request.getVehicleNo());
        assignment.setDriverId(request.getDriverId());
        assignment.setActive(request.isActive());
        return ResponseEntity.ok(assignmentRepository.save(assignment));
    }

    @DeleteMapping("/assignments/{id}")
    public ResponseEntity<?> deleteAssignment(@PathVariable Long id) {
        assignmentRepository.deleteById(id);
        return ResponseEntity.ok(Map.of("ok", true));
    }

    private boolean isAdmin(HttpServletRequest request) {
        Object profileObj = request.getAttribute("userProfile");
        if (profileObj instanceof UserProfile profile) {
            return "admin".equalsIgnoreCase(profile.getRole());
        }
        return false;
    }
}
