package com.company.schoolbackend.service;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

@Service
public class R2Service {
    private final S3Presigner presigner;
    private final String bucket;
    private final String prefix;
    private final int maxUploadKb;

    public R2Service(
            S3Presigner presigner,
            @Value("${r2.bucket}") String bucket,
            @Value("${r2.prefix:student_prof_pic/}") String prefix,
            @Value("${r2.max-upload-kb:600}") int maxUploadKb
    ) {
        this.presigner = presigner;
        this.bucket = bucket;
        this.prefix = prefix.endsWith("/") ? prefix : prefix + "/";
        this.maxUploadKb = maxUploadKb;
    }

    public PresignedPutObjectRequest createUploadUrl(String contentType, String fileName, String objectKey) {
        String extension = ".jpg";
        if (fileName != null && fileName.contains(".")) {
            String ext = fileName.substring(fileName.lastIndexOf("."));
            if (ext.length() <= 5) {
                extension = ext.toLowerCase();
            }
        }
        PutObjectRequest putRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(objectKey)
                .contentType(contentType)
                .build();
        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(10))
                .putObjectRequest(putRequest)
                .build();
        return presigner.presignPutObject(presignRequest);
    }

    public String generateObjectKey(String fileName, String customPrefix) {
        String prefixValue = customPrefix == null || customPrefix.isBlank() ? prefix : customPrefix;
        if (!prefixValue.endsWith("/")) {
            prefixValue = prefixValue + "/";
        }
        String extension = ".jpg";
        if (fileName != null && fileName.contains(".")) {
            String ext = fileName.substring(fileName.lastIndexOf("."));
            if (ext.length() <= 5) {
                extension = ext.toLowerCase();
            }
        }
        return prefixValue + UUID.randomUUID() + extension;
    }

    public String generateObjectKey(String fileName) {
        String extension = ".jpg";
        if (fileName != null && fileName.contains(".")) {
            String ext = fileName.substring(fileName.lastIndexOf("."));
            if (ext.length() <= 5) {
                extension = ext.toLowerCase();
            }
        }
        return prefix + UUID.randomUUID() + extension;
    }

    public String getBucket() {
        return bucket;
    }

    public PresignedGetObjectRequest createDownloadUrl(String objectKey) {
        GetObjectRequest getRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(objectKey)
                .build();
        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(10))
                .getObjectRequest(getRequest)
                .build();
        return presigner.presignGetObject(presignRequest);
    }

    public int getMaxUploadKb() {
        return maxUploadKb;
    }

    public String formatExpiry(PresignedPutObjectRequest request) {
        return OffsetDateTime.now().plusMinutes(10).toString();
    }
}
