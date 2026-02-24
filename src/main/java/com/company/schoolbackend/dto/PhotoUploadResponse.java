package com.company.schoolbackend.dto;

public class PhotoUploadResponse {
    private String uploadUrl;
    private String objectKey;
    private String expiresAt;

    public PhotoUploadResponse() {}

    public PhotoUploadResponse(String uploadUrl, String objectKey, String expiresAt) {
        this.uploadUrl = uploadUrl;
        this.objectKey = objectKey;
        this.expiresAt = expiresAt;
    }

    public String getUploadUrl() {
        return uploadUrl;
    }

    public void setUploadUrl(String uploadUrl) {
        this.uploadUrl = uploadUrl;
    }

    public String getObjectKey() {
        return objectKey;
    }

    public void setObjectKey(String objectKey) {
        this.objectKey = objectKey;
    }

    public String getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(String expiresAt) {
        this.expiresAt = expiresAt;
    }
}
