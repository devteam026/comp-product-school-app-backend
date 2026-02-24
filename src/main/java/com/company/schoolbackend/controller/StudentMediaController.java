package com.company.schoolbackend.controller;

import com.company.schoolbackend.dto.PhotoUploadResponse;
import com.company.schoolbackend.entity.Student;
import com.company.schoolbackend.repository.StudentRepository;
import com.company.schoolbackend.service.R2Service;
import java.time.OffsetDateTime;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;

@RestController
public class StudentMediaController {
    private final R2Service r2Service;
    private final StudentRepository studentRepository;

    public StudentMediaController(R2Service r2Service, StudentRepository studentRepository) {
        this.r2Service = r2Service;
        this.studentRepository = studentRepository;
    }

    @PostMapping("/api/students/photo-upload")
    public ResponseEntity<?> createUploadUrlForNewStudent(
            @RequestParam String contentType,
            @RequestParam String fileName,
            @RequestParam long sizeBytes
    ) {
        long maxBytes = r2Service.getMaxUploadKb() * 1024L;
        if (sizeBytes > maxBytes) {
            return ResponseEntity.badRequest()
                    .body(java.util.Map.of("error", "File too large"));
        }
        String objectKey = r2Service.generateObjectKey(fileName);
        PresignedPutObjectRequest presigned = r2Service.createUploadUrl(contentType, fileName, objectKey);
        PhotoUploadResponse response = new PhotoUploadResponse(
                presigned.url().toString(),
                objectKey,
                OffsetDateTime.now().plusMinutes(10).toString()
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/students/{id}/photo-upload")
    public ResponseEntity<?> createUploadUrl(
            @PathVariable String id,
            @RequestParam String contentType,
            @RequestParam String fileName,
            @RequestParam long sizeBytes
    ) {
        Student student = studentRepository.findById(id).orElse(null);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        long maxBytes = r2Service.getMaxUploadKb() * 1024L;
        if (sizeBytes > maxBytes) {
            return ResponseEntity.badRequest()
                    .body(java.util.Map.of("error", "File too large"));
        }
        String objectKey = r2Service.generateObjectKey(fileName);
        PresignedPutObjectRequest presigned = r2Service.createUploadUrl(contentType, fileName, objectKey);
        PhotoUploadResponse response = new PhotoUploadResponse(
                presigned.url().toString(),
                objectKey,
                OffsetDateTime.now().plusMinutes(10).toString()
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/students/{id}/photo-url")
    public ResponseEntity<?> getPhotoUrl(@PathVariable String id) {
        Student student = studentRepository.findById(id).orElse(null);
        if (student == null || student.getProfilePhotoKey() == null || student.getProfilePhotoKey().isBlank()) {
            return ResponseEntity.ok(java.util.Map.of("url", ""));
        }
        PresignedGetObjectRequest presigned = r2Service.createDownloadUrl(student.getProfilePhotoKey());
        return ResponseEntity.ok(java.util.Map.of("url", presigned.url().toString()));
    }
}
