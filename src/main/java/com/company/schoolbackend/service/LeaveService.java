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
        if ((request.getPeriodType() == null) != (request.getMaxPerPeriod() == null)) {
            throw new IllegalArgumentException("Period type and max per period are required together");
        }
        LeaveCategory category = id == null ? new LeaveCategory() : categoryRepository.findById(id).orElse(new LeaveCategory());
        category.setName(request.getName().trim());
        category.setRole(request.getRole().trim().toLowerCase());
        category.setMaxDays(request.getMaxDays());
        if (request.getPeriodType() != null) {
            String periodType = request.getPeriodType().trim().toUpperCase();
            if (!periodType.equals("MONTHLY") && !periodType.equals("YEARLY")) {
                throw new IllegalArgumentException("Invalid period type");
            }
            category.setPeriodType(periodType);
            category.setMaxPerPeriod(request.getMaxPerPeriod());
        } else {
            category.setPeriodType(null);
            category.setMaxPerPeriod(null);
        }
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
        LocalDate from = LocalDate.parse(request.getFromDate());
        LocalDate to = LocalDate.parse(request.getToDate());
        if (from.isAfter(to)) {
            throw new IllegalArgumentException("Start date must be before or equal to end date");
        }
        if (from.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Start date cannot be in the past");
        }
        AppUser user = requireUser(username);
        Long employeeId = Long.parseLong(user.getUsername());
        Employee employee = employeeRepository.findById(employeeId).orElseThrow();
        LeaveCategory category = categoryRepository.findById(request.getCategoryId()).orElseThrow();
        if (!category.isActive()) {
            throw new IllegalArgumentException("Leave category inactive");
        }
        enforcePeriodLimit(employee.getId(), category, from);
        LeaveRequest entity = new LeaveRequest();
        entity.setEmployeeId(employee.getId());
        entity.setUserId(user.getId());
        entity.setRole(user.getRole().name().toLowerCase());
        entity.setCategoryId(category.getId());
        entity.setFromDate(from);
        entity.setToDate(to);
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
        String currentStatus = entity.getStatus();
        String targetStatus = request.getStatus() == null ? "" : request.getStatus().toUpperCase();
        if ("REJECTED".equalsIgnoreCase(currentStatus)) {
            throw new IllegalArgumentException("Leave already rejected");
        }
        if ("APPROVED".equalsIgnoreCase(currentStatus)) {
            boolean isFuture = entity.getFromDate() != null && entity.getFromDate().isAfter(LocalDate.now());
            if (!"REJECTED".equalsIgnoreCase(targetStatus) || !isFuture) {
                throw new IllegalArgumentException("Approved leave cannot be changed");
            }
        }
        AppUser reviewer = requireUser(reviewerUsername);
        entity.setStatus(targetStatus);
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

    private void enforcePeriodLimit(Long employeeId, LeaveCategory category, LocalDate fromDate) {
        if (category.getPeriodType() == null || category.getMaxPerPeriod() == null) {
            return;
        }
        LocalDate date = fromDate;
        LocalDate start;
        LocalDate end;
        if ("MONTHLY".equalsIgnoreCase(category.getPeriodType())) {
            start = date.withDayOfMonth(1);
            end = date.withDayOfMonth(date.lengthOfMonth());
        } else {
            start = LocalDate.of(date.getYear(), 1, 1);
            end = LocalDate.of(date.getYear(), 12, 31);
        }
        long totalDays = requestRepository
                .findByEmployeeIdAndCategoryIdAndFromDateLessThanEqualAndToDateGreaterThanEqualAndStatusNot(
                        employeeId,
                        category.getId(),
                        end,
                        start,
                        "REJECTED"
                )
                .stream()
                .mapToLong(req -> {
                    LocalDate reqStart = req.getFromDate().isAfter(start) ? req.getFromDate() : start;
                    LocalDate reqEnd = req.getToDate().isBefore(end) ? req.getToDate() : end;
                    return reqStart.isAfter(reqEnd) ? 0 : reqEnd.toEpochDay() - reqStart.toEpochDay() + 1;
                })
                .sum();
        if (totalDays >= category.getMaxPerPeriod()) {
            throw new IllegalArgumentException("Leave limit reached for this period");
        }
    }

    private LeaveCategoryDto toDto(LeaveCategory category) {
        LeaveCategoryDto dto = new LeaveCategoryDto();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setRole(category.getRole());
        dto.setMaxDays(category.getMaxDays());
        dto.setPeriodType(category.getPeriodType());
        dto.setMaxPerPeriod(category.getMaxPerPeriod());
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
