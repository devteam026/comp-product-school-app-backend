package com.company.schoolbackend.controller;

import com.company.schoolbackend.dto.LeaveCategoryDto;
import com.company.schoolbackend.dto.LeaveCategoryRequest;
import com.company.schoolbackend.dto.LeaveDecisionRequest;
import com.company.schoolbackend.dto.LeaveRequestCreate;
import com.company.schoolbackend.dto.LeaveRequestResponse;
import com.company.schoolbackend.dto.UserProfile;
import com.company.schoolbackend.service.LeaveService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/leaves")
public class LeaveController {
    private final LeaveService leaveService;

    public LeaveController(LeaveService leaveService) {
        this.leaveService = leaveService;
    }

    @GetMapping("/categories")
    public ResponseEntity<List<LeaveCategoryDto>> listCategories(
            @RequestParam(value = "role", required = false) String role,
            @RequestParam(value = "includeInactive", required = false, defaultValue = "false") boolean includeInactive,
            HttpServletRequest request
    ) {
        if (!isAdmin(request) && includeInactive) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return ResponseEntity.ok(leaveService.listCategories(role, includeInactive));
    }

    @PostMapping("/categories")
    public ResponseEntity<LeaveCategoryDto> createCategory(
            @RequestBody LeaveCategoryRequest requestBody,
            HttpServletRequest request
    ) {
        if (!isAdmin(request)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return ResponseEntity.ok(leaveService.upsertCategory(null, requestBody));
    }

    @PutMapping("/categories/{id}")
    public ResponseEntity<LeaveCategoryDto> updateCategory(
            @PathVariable Long id,
            @RequestBody LeaveCategoryRequest requestBody,
            HttpServletRequest request
    ) {
        if (!isAdmin(request)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return ResponseEntity.ok(leaveService.upsertCategory(id, requestBody));
    }

    @DeleteMapping("/categories/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id, HttpServletRequest request) {
        if (!isAdmin(request)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        leaveService.deleteCategory(id);
        return ResponseEntity.ok(java.util.Map.of("ok", true));
    }

    @PostMapping
    public ResponseEntity<LeaveRequestResponse> applyLeave(
            @RequestBody LeaveRequestCreate requestBody,
            HttpServletRequest request
    ) {
        UserProfile profile = getProfile(request);
        if (profile == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return ResponseEntity.ok(leaveService.applyLeave(profile.getUsername(), requestBody));
    }

    @GetMapping
    public ResponseEntity<List<LeaveRequestResponse>> listLeaves(HttpServletRequest request) {
        UserProfile profile = getProfile(request);
        if (profile == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        boolean admin = "admin".equalsIgnoreCase(profile.getRole());
        return ResponseEntity.ok(leaveService.listRequests(profile.getUsername(), admin));
    }

    @PostMapping("/{id}/decision")
    public ResponseEntity<LeaveRequestResponse> decide(
            @PathVariable Long id,
            @RequestBody LeaveDecisionRequest requestBody,
            HttpServletRequest request
    ) {
        UserProfile profile = getProfile(request);
        if (profile == null || !"admin".equalsIgnoreCase(profile.getRole())) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return ResponseEntity.ok(leaveService.decide(id, profile.getUsername(), requestBody));
    }

    private boolean isAdmin(HttpServletRequest request) {
        UserProfile profile = getProfile(request);
        return profile != null && "admin".equalsIgnoreCase(profile.getRole());
    }

    private UserProfile getProfile(HttpServletRequest request) {
        Object profileObj = request.getAttribute("userProfile");
        if (profileObj instanceof UserProfile profile) {
            return profile;
        }
        return null;
    }
}
