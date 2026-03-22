package com.company.schoolbackend.controller;

import com.company.schoolbackend.dto.PhotoUploadResponse;
import com.company.schoolbackend.dto.UserProfile;
import com.company.schoolbackend.entity.LeaveRequest;
import com.company.schoolbackend.repository.LeaveRequestRepository;
import com.company.schoolbackend.service.R2Service;
import jakarta.servlet.http.HttpServletRequest;
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
@RequestMapping("/api/leaves")
public class LeaveMediaController {
    private static final String LEAVE_PREFIX = "leave_attachments/";

    private final R2Service r2Service;
    private final LeaveRequestRepository leaveRequestRepository;

    public LeaveMediaController(R2Service r2Service, LeaveRequestRepository leaveRequestRepository) {
        this.r2Service = r2Service;
        this.leaveRequestRepository = leaveRequestRepository;
    }

    @PostMapping("/upload")
    public ResponseEntity<PhotoUploadResponse> createUpload(
            @RequestParam("contentType") String contentType,
            @RequestParam("fileName") String fileName,
            @RequestParam("sizeBytes") long sizeBytes,
            HttpServletRequest request
    ) {
        if (!isAuthenticated(request)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        if (sizeBytes > (long) r2Service.getMaxUploadKb() * 1024L) {
            return new ResponseEntity<>(HttpStatus.PAYLOAD_TOO_LARGE);
        }
        String objectKey = r2Service.generateObjectKey(fileName, LEAVE_PREFIX);
        PresignedPutObjectRequest presigned = r2Service.createUploadUrl(contentType, fileName, objectKey);
        PhotoUploadResponse response = new PhotoUploadResponse(presigned.url().toString(), objectKey, r2Service.formatExpiry(presigned));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/attachment-url")
    public ResponseEntity<Map<String, String>> getAttachment(@PathVariable Long id, HttpServletRequest request) {
        if (!isAuthenticated(request)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        LeaveRequest leave = leaveRequestRepository.findById(id).orElse(null);
        if (leave == null || leave.getAttachmentKey() == null || leave.getAttachmentKey().isBlank()) {
            return ResponseEntity.ok(Map.of("url", ""));
        }
        PresignedGetObjectRequest presigned = r2Service.createDownloadUrl(leave.getAttachmentKey());
        return ResponseEntity.ok(Map.of("url", presigned.url().toString()));
    }

    private boolean isAuthenticated(HttpServletRequest request) {
        Object profileObj = request.getAttribute("userProfile");
        return profileObj instanceof UserProfile;
    }
}
