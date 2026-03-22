package com.company.schoolbackend.service;

import com.company.schoolbackend.dto.LeaveCategoryDto;
import com.company.schoolbackend.dto.LeaveCategoryRequest;
import com.company.schoolbackend.dto.LeaveDecisionRequest;
import com.company.schoolbackend.dto.LeaveRequestCreate;
import com.company.schoolbackend.dto.LeaveRequestResponse;
import com.company.schoolbackend.entity.AppUser;
import com.company.schoolbackend.entity.Employee;
import com.company.schoolbackend.entity.LeaveCategory;
import com.company.schoolbackend.entity.LeaveRequest;
import com.company.schoolbackend.repository.AppUserRepository;
import com.company.schoolbackend.repository.EmployeeRepository;
import com.company.schoolbackend.repository.LeaveCategoryRepository;
import com.company.schoolbackend.repository.LeaveRequestRepository;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class LeaveService {
    private final LeaveCategoryRepository categoryRepository;
    private final LeaveRequestRepository requestRepository;
    private final EmployeeRepository employeeRepository;
    private final AppUserRepository appUserRepository;

    public LeaveService(
            LeaveCategoryRepository categoryRepository,
            LeaveRequestRepository requestRepository,
            EmployeeRepository employeeRepository,
            AppUserRepository appUserRepository
    ) {
        this.categoryRepository = categoryRepository;
        this.requestRepository = requestRepository;
        this.employeeRepository = employeeRepository;
        this.appUserRepository = appUserRepository;
    }

    public List<LeaveCategoryDto> listCategories(String role, boolean includeInactive) {
        List<LeaveCategory> categories = includeInactive
                ? categoryRepository.findAll()
                : (role == null || role.isBlank()
                    ? categoryRepository.findByActiveTrueOrderByNameAsc()
                    : categoryRepository.findByRoleAndActiveTrueOrderByNameAsc(role));
        return categories.stream().map(this::toDto).collect(Collectors.toList());
    }

    public LeaveCategoryDto upsertCategory(Long id, LeaveCategoryRequest request) {
        if (request == null || request.getName() == null || request.getName().isBlank()
                || request.getRole() == null || request.getRole().isBlank()
                || request.getMaxDays() == null) {
            throw new IllegalArgumentException("Missing fields");
        }
        LeaveCategory category = id == null ? new LeaveCategory() : categoryRepository.findById(id).orElse(new LeaveCategory());
        category.setName(request.getName().trim());
        category.setRole(request.getRole().trim().toLowerCase());
        category.setMaxDays(request.getMaxDays());
        if (request.getActive() != null) {
            category.setActive(request.getActive());
        }
        return toDto(categoryRepository.save(category));
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    public LeaveRequestResponse applyLeave(String username, LeaveRequestCreate request) {
        if (request == null || request.getCategoryId() == null || request.getFromDate() == null || request.getToDate() == null) {
            throw new IllegalArgumentException("Missing fields");
        }
        AppUser user = requireUser(username);
        Long employeeId = Long.parseLong(user.getUsername());
        Employee employee = employeeRepository.findById(employeeId).orElseThrow();
        LeaveCategory category = categoryRepository.findById(request.getCategoryId()).orElseThrow();
        if (!category.isActive()) {
            throw new IllegalArgumentException("Leave category inactive");
        }
        LeaveRequest entity = new LeaveRequest();
        entity.setEmployeeId(employee.getId());
        entity.setUserId(user.getId());
        entity.setRole(user.getRole().name().toLowerCase());
        entity.setCategoryId(category.getId());
        entity.setFromDate(LocalDate.parse(request.getFromDate()));
        entity.setToDate(LocalDate.parse(request.getToDate()));
        entity.setReason(request.getReason());
        entity.setAttachmentKey(request.getAttachmentKey());
        entity.setStatus("PENDING");
        entity.setAppliedAt(OffsetDateTime.now());
        return toResponse(requestRepository.save(entity), category);
    }

    public List<LeaveRequestResponse> listRequests(String username, boolean adminView) {
        List<LeaveRequest> requests;
        if (adminView) {
            requests = requestRepository.findAll();
        } else {
            AppUser user = requireUser(username);
            Long employeeId = Long.parseLong(user.getUsername());
            requests = requestRepository.findByEmployeeIdOrderByAppliedAtDesc(employeeId);
        }
        Map<Long, LeaveCategory> categoryMap = categoryRepository.findAll().stream()
                .collect(Collectors.toMap(LeaveCategory::getId, c -> c));
        return requests.stream()
                .map(r -> toResponse(r, categoryMap.get(r.getCategoryId())))
                .collect(Collectors.toList());
    }

    public LeaveRequestResponse decide(Long id, String reviewerUsername, LeaveDecisionRequest request) {
        LeaveRequest entity = requestRepository.findById(id).orElseThrow();
        if (entity.getStatus().equalsIgnoreCase("APPROVED") || entity.getStatus().equalsIgnoreCase("REJECTED")) {
            throw new IllegalArgumentException("Leave already processed");
        }
        AppUser reviewer = requireUser(reviewerUsername);
        entity.setStatus(request.getStatus().toUpperCase());
        entity.setReviewerNote(request.getReviewerNote());
        entity.setReviewedBy(reviewer.getId());
        entity.setReviewedAt(OffsetDateTime.now());
        LeaveCategory category = categoryRepository.findById(entity.getCategoryId()).orElse(null);
        return toResponse(requestRepository.save(entity), category);
    }

    private AppUser requireUser(String username) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Login user not found");
        }
        return appUserRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("Login user not found"));
    }

    private LeaveCategoryDto toDto(LeaveCategory category) {
        LeaveCategoryDto dto = new LeaveCategoryDto();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setRole(category.getRole());
        dto.setMaxDays(category.getMaxDays());
        dto.setActive(category.isActive());
        return dto;
    }

    private LeaveRequestResponse toResponse(LeaveRequest request, LeaveCategory category) {
        LeaveRequestResponse response = new LeaveRequestResponse();
        response.setId(request.getId());
        response.setEmployeeId(request.getEmployeeId());
        response.setCategoryId(request.getCategoryId());
        response.setCategoryName(category == null ? "" : category.getName());
        response.setRole(request.getRole());
        response.setFromDate(request.getFromDate().toString());
        response.setToDate(request.getToDate().toString());
        response.setReason(request.getReason());
        response.setAttachmentKey(request.getAttachmentKey());
        response.setStatus(request.getStatus());
        response.setAppliedAt(request.getAppliedAt() == null ? "" : request.getAppliedAt().toString());
        response.setReviewedAt(request.getReviewedAt() == null ? "" : request.getReviewedAt().toString());
        response.setReviewedBy(request.getReviewedBy());
        response.setReviewerNote(request.getReviewerNote());
        return response;
    }
}
