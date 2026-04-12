package com.company.schoolbackend.service;

import com.company.schoolbackend.dto.SchoolProfileRequest;
import com.company.schoolbackend.dto.SchoolProfileResponse;
import com.company.schoolbackend.entity.SchoolProfile;
import com.company.schoolbackend.repository.SchoolProfileRepository;
import java.time.OffsetDateTime;
import org.springframework.stereotype.Service;

@Service
public class SchoolProfileService {
    private static final long PROFILE_ID = 1L;
    private final SchoolProfileRepository repository;

    public SchoolProfileService(SchoolProfileRepository repository) {
        this.repository = repository;
    }

    public SchoolProfileResponse getProfile() {
        SchoolProfile profile = repository.findById(PROFILE_ID).orElseGet(() -> {
            SchoolProfile created = new SchoolProfile();
            created.setId(PROFILE_ID);
            created.setSchoolName("Your School");
            created.setAbout("");
            created.setMission("");
            created.setVision("");
            created.setLogoUrl("");
            created.setCampusImageUrl("");
            created.setSchoolUrl("");
            created.setSidebarBg("#0f172a");
            created.setBrandName("School Portal");
            created.setAppTitle("School Portal");
            created.setAppDescription("School management dashboard");
            created.setAddress("");
            created.setPhone("");
            created.setUpdatedAt(OffsetDateTime.now());
            return repository.save(created);
        });
        return toResponse(profile);
    }

    public SchoolProfileResponse upsert(SchoolProfileRequest request) {
        SchoolProfile profile = repository.findById(PROFILE_ID).orElseGet(() -> {
            SchoolProfile created = new SchoolProfile();
            created.setId(PROFILE_ID);
            return created;
        });
        if (request.getSchoolName() != null) {
            profile.setSchoolName(request.getSchoolName().trim());
        }
        profile.setAbout(nullToEmpty(request.getAbout()));
        profile.setMission(nullToEmpty(request.getMission()));
        profile.setVision(nullToEmpty(request.getVision()));
        profile.setLogoUrl(nullToEmpty(request.getLogoUrl()));
        profile.setCampusImageUrl(nullToEmpty(request.getCampusImageUrl()));
        profile.setSchoolUrl(nullToEmpty(request.getSchoolUrl()));
        profile.setSidebarBg(nullToEmpty(request.getSidebarBg()));
        profile.setBrandName(nullToEmpty(request.getBrandName()));
        profile.setAppTitle(nullToEmpty(request.getAppTitle()));
        profile.setAppDescription(nullToEmpty(request.getAppDescription()));
        profile.setAddress(nullToEmpty(request.getAddress()));
        profile.setPhone(nullToEmpty(request.getPhone()));
        profile.setUpdatedAt(OffsetDateTime.now());
        return toResponse(repository.save(profile));
    }

    private SchoolProfileResponse toResponse(SchoolProfile profile) {
        SchoolProfileResponse response = new SchoolProfileResponse();
        response.setSchoolName(profile.getSchoolName());
        response.setAbout(profile.getAbout());
        response.setMission(profile.getMission());
        response.setVision(profile.getVision());
        response.setLogoUrl(profile.getLogoUrl());
        response.setCampusImageUrl(profile.getCampusImageUrl());
        response.setSchoolUrl(profile.getSchoolUrl());
        response.setSidebarBg(profile.getSidebarBg());
        response.setBrandName(profile.getBrandName());
        response.setAppTitle(profile.getAppTitle());
        response.setAppDescription(profile.getAppDescription());
        response.setAddress(profile.getAddress());
        response.setPhone(profile.getPhone());
        return response;
    }

    private static String nullToEmpty(String value) {
        return value == null ? "" : value.trim();
    }
}
