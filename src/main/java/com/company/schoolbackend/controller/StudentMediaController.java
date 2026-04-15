package com.company.schoolbackend.controller;

import com.company.schoolbackend.entity.Student;
import com.company.schoolbackend.repository.StudentRepository;
import com.company.schoolbackend.service.R2Service;
import java.util.Map;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

@RestController
public class StudentMediaController {
    private final R2Service r2Service;
    private final StudentRepository studentRepository;

    public StudentMediaController(R2Service r2Service, StudentRepository studentRepository) {
        this.r2Service = r2Service;
        this.studentRepository = studentRepository;
    }

    /**
     * Upload photo server-side (browser → backend → R2).
     * Eliminates the need for CORS on the R2 bucket.
     */
    @PostMapping(value = "/api/students/photo-upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadPhotoForNewStudent(
            @RequestParam("file") MultipartFile file
    ) {
        long maxBytes = r2Service.getMaxUploadKb() * 1024L;
        if (file.getSize() > maxBytes) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "File too large. Max " + r2Service.getMaxUploadKb() + " KB."));
        }
        try {
            String objectKey = r2Service.generateObjectKey(file.getOriginalFilename());
            String contentType = file.getContentType() != null ? file.getContentType() : "image/jpeg";
            r2Service.uploadFile(objectKey, file.getBytes(), contentType);
            return ResponseEntity.ok(Map.of("objectKey", objectKey));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Upload failed: " + e.getMessage()));
        }
    }

    /**
     * Upload photo for an existing student (same server-side flow).
     */
    @PostMapping(value = "/api/students/{id}/photo-upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadPhotoForStudent(
            @PathVariable String id,
            @RequestParam("file") MultipartFile file
    ) {
        Student student = studentRepository.findById(id).orElse(null);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        long maxBytes = r2Service.getMaxUploadKb() * 1024L;
        if (file.getSize() > maxBytes) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "File too large. Max " + r2Service.getMaxUploadKb() + " KB."));
        }
        try {
            String objectKey = r2Service.generateObjectKey(file.getOriginalFilename());
            String contentType = file.getContentType() != null ? file.getContentType() : "image/jpeg";
            r2Service.uploadFile(objectKey, file.getBytes(), contentType);
            return ResponseEntity.ok(Map.of("objectKey", objectKey));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Upload failed: " + e.getMessage()));
        }
    }

    @GetMapping("/api/students/{id}/photo-url")
    public ResponseEntity<?> getPhotoUrl(@PathVariable String id) {
        Student student = studentRepository.findById(id).orElse(null);
        if (student == null || student.getProfilePhotoKey() == null || student.getProfilePhotoKey().isBlank()) {
            return ResponseEntity.ok(Map.of("url", ""));
        }
        PresignedGetObjectRequest presigned = r2Service.createDownloadUrl(student.getProfilePhotoKey());
        return ResponseEntity.ok(Map.of("url", presigned.url().toString()));
    }
}
