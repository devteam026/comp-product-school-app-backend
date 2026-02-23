package com.company.schoolbackend.controller;

import com.company.schoolbackend.dto.SchoolProfileRequest;
import com.company.schoolbackend.dto.SchoolProfileResponse;
import com.company.schoolbackend.service.SchoolProfileService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/school")
public class SchoolProfileController {
    private final SchoolProfileService service;

    public SchoolProfileController(SchoolProfileService service) {
        this.service = service;
    }

    @GetMapping
    public SchoolProfileResponse getProfile() {
        return service.getProfile();
    }

    @PutMapping
    public SchoolProfileResponse updateProfile(@RequestBody SchoolProfileRequest request) {
        return service.upsert(request);
    }
}
