package com.company.schoolbackend.controller;

import com.company.schoolbackend.dto.PhotoUploadResponse;
import com.company.schoolbackend.dto.UserProfile;
import com.company.schoolbackend.entity.Employee;
import com.company.schoolbackend.repository.EmployeeRepository;
import com.company.schoolbackend.service.R2Service;
import jakarta.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;

@RestController
@RequestMapping("/api/employees")
public class EmployeeMediaController {
    private static final String EMPLOYEE_PREFIX = "employee_files/";

    private final R2Service r2Service;
    private final EmployeeRepository employeeRepository;

    public EmployeeMediaController(R2Service r2Service, EmployeeRepository employeeRepository) {
        this.r2Service = r2Service;
        this.employeeRepository = employeeRepository;
    }

    @PostMapping("/upload")
    public ResponseEntity<PhotoUploadResponse> createUpload(
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
        String objectKey = r2Service.generateObjectKey(fileName, EMPLOYEE_PREFIX + type + "/");
        PresignedPutObjectRequest presigned = r2Service.createUploadUrl(contentType, fileName, objectKey);
        PhotoUploadResponse response = new PhotoUploadResponse(presigned.url().toString(), objectKey, r2Service.formatExpiry(presigned));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/file-url")
    public ResponseEntity<Map<String, String>> getFileUrl(
            @PathVariable Long id,
            @RequestParam("type") String type,
            HttpServletRequest request
    ) {
        if (!isAdmin(request)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        Employee employee = employeeRepository.findById(id).orElse(null);
        if (employee == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        String key = switch (type) {
            case "profile" -> employee.getProfilePhotoKey();
            case "contract" -> employee.getContractDocKey();
            case "idProof" -> employee.getIdProofKey();
            case "resume" -> employee.getResumeKey();
            default -> null;
        };
        if (key == null || key.isBlank()) {
            return ResponseEntity.ok(Map.of("url", ""));
        }
        PresignedGetObjectRequest presigned = r2Service.createDownloadUrl(key);
        return ResponseEntity.ok(Map.of("url", presigned.url().toString()));
    }

    private boolean isAdmin(HttpServletRequest request) {
        Object profileObj = request.getAttribute("userProfile");
        if (profileObj instanceof UserProfile profile) {
            return "admin".equalsIgnoreCase(profile.getRole());
        }
        return false;
    }
}
