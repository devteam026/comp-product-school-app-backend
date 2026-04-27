package com.company.schoolbackend.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.company.schoolbackend.dto.PhotoUploadResponse;
import com.company.schoolbackend.dto.UserProfile;
import com.company.schoolbackend.entity.Employee;
import com.company.schoolbackend.repository.EmployeeRepository;
import com.company.schoolbackend.service.R2Service;
import jakarta.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;

@RestController
@RequestMapping("/api/employees")
public class EmployeeMediaController {
    private static final Logger log = LoggerFactory.getLogger(EmployeeMediaController.class);
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

    private static final long PHOTO_MAX_BYTES = 5L * 1024 * 1024;   // 5MB
    private static final long DOC_MAX_BYTES   = 10L * 1024 * 1024;  // 10MB

    @PostMapping(value = "/photo-upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadPhoto(
            @RequestParam("file") MultipartFile file,
            HttpServletRequest request
    ) {
        if (!isAdmin(request)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        if (file.getSize() > PHOTO_MAX_BYTES) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Photo too large. Max 5 MB."));
        }
        String ct = file.getContentType();
        if (ct == null || !ct.startsWith("image/")) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Only image files are allowed for profile photo."));
        }
        try {
            String objectKey = r2Service.generateObjectKey(file.getOriginalFilename(), EMPLOYEE_PREFIX + "profile/");
            r2Service.uploadFile(objectKey, file.getBytes(), ct);
            return ResponseEntity.ok(Map.of("objectKey", objectKey));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Upload failed: " + e.getMessage()));
        }
    }

    @PostMapping(value = "/doc-upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadDocument(
            @RequestParam("file") MultipartFile file,
            @RequestParam("type") String type,
            HttpServletRequest request
    ) {
        log.info("doc-upload: type={}, fileName={}, size={}, contentType={}",
                type, file.getOriginalFilename(), file.getSize(), file.getContentType());
        if (!isAdmin(request)) {
            log.warn("doc-upload: forbidden - not admin");
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        if (file.getSize() > DOC_MAX_BYTES) {
            log.warn("doc-upload: file too large: {} bytes", file.getSize());
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "File too large. Max 10 MB."));
        }
        try {
            String objectKey = r2Service.generateObjectKey(file.getOriginalFilename(), EMPLOYEE_PREFIX + type + "/");
            String contentType = file.getContentType() != null ? file.getContentType() : "application/octet-stream";
            log.info("doc-upload: uploading to R2 key={}, contentType={}", objectKey, contentType);
            r2Service.uploadFile(objectKey, file.getBytes(), contentType);
            log.info("doc-upload: success key={}", objectKey);
            return ResponseEntity.ok(Map.of("objectKey", objectKey));
        } catch (Exception e) {
            log.error("doc-upload: failed", e);
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Upload failed: " + e.getMessage()));
        }
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
