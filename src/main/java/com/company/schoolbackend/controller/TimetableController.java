package com.company.schoolbackend.controller;

import com.company.schoolbackend.dto.TimetableAssignmentDto;
import com.company.schoolbackend.dto.TimetableBulkRequest;
import com.company.schoolbackend.dto.TimetableCopyRequest;
import com.company.schoolbackend.dto.TimetableCreateRequest;
import com.company.schoolbackend.dto.TimetableLockRequest;
import com.company.schoolbackend.dto.TimetablePeriodRequest;
import com.company.schoolbackend.dto.TimetableResponse;
import com.company.schoolbackend.dto.TimetableSubjectRequest;
import com.company.schoolbackend.dto.PeriodDto;
import com.company.schoolbackend.dto.SubjectDto;
import com.company.schoolbackend.dto.UserProfile;
import com.company.schoolbackend.service.TimetableService;
import jakarta.servlet.http.HttpServletRequest;
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
@RequestMapping("/api/timetable")
public class TimetableController {
    private final TimetableService timetableService;

    public TimetableController(TimetableService timetableService) {
        this.timetableService = timetableService;
    }

    @GetMapping
    public ResponseEntity<TimetableResponse> get(
            @RequestParam(value = "week", required = false) String week,
            @RequestParam(value = "classId", required = false) Long classId,
            @RequestParam(value = "teacherId", required = false) Long teacherId,
            @RequestParam(value = "subjectId", required = false) Long subjectId,
            HttpServletRequest request
    ) {
        if (!isAdmin(request)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return ResponseEntity.ok(timetableService.getTimetable(week, classId, teacherId, subjectId));
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody TimetableCreateRequest requestBody, HttpServletRequest request) {
        if (!isAdmin(request)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        TimetableAssignmentDto dto = timetableService.create(requestBody);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody TimetableCreateRequest requestBody, HttpServletRequest request) {
        if (!isAdmin(request)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        TimetableAssignmentDto dto = timetableService.update(id, requestBody);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, HttpServletRequest request) {
        if (!isAdmin(request)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        timetableService.delete(id);
        return ResponseEntity.ok(java.util.Map.of("ok", true));
    }

    @PostMapping("/bulk")
    public ResponseEntity<?> bulk(@RequestBody TimetableBulkRequest requestBody, HttpServletRequest request) {
        if (!isAdmin(request)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        timetableService.bulk(requestBody);
        return ResponseEntity.ok(java.util.Map.of("ok", true));
    }

    @PostMapping("/copy")
    public ResponseEntity<?> copy(@RequestBody TimetableCopyRequest requestBody, HttpServletRequest request) {
        if (!isAdmin(request)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        timetableService.copy(requestBody);
        return ResponseEntity.ok(java.util.Map.of("ok", true));
    }

    @PostMapping("/lock")
    public ResponseEntity<?> lock(@RequestBody TimetableLockRequest requestBody, HttpServletRequest request) {
        if (!isAdmin(request)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        timetableService.setLock(requestBody.getAssignmentId(), requestBody.isLocked());
        return ResponseEntity.ok(java.util.Map.of("ok", true));
    }

    @GetMapping("/subjects")
    public ResponseEntity<java.util.List<SubjectDto>> listSubjects(
            @RequestParam(value = "classId", required = false) Long classId,
            HttpServletRequest request
    ) {
        if (!isAdmin(request)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return ResponseEntity.ok(timetableService.listSubjects(classId));
    }

    @PostMapping("/subjects")
    public ResponseEntity<SubjectDto> createSubject(
            @RequestBody TimetableSubjectRequest requestBody,
            HttpServletRequest request
    ) {
        if (!isAdmin(request)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return ResponseEntity.ok(timetableService.createSubject(requestBody));
    }

    @PutMapping("/subjects/{id}")
    public ResponseEntity<SubjectDto> updateSubject(
            @PathVariable Long id,
            @RequestBody TimetableSubjectRequest requestBody,
            HttpServletRequest request
    ) {
        if (!isAdmin(request)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return ResponseEntity.ok(timetableService.updateSubject(id, requestBody));
    }

    @DeleteMapping("/subjects/{id}")
    public ResponseEntity<?> deleteSubject(@PathVariable Long id, HttpServletRequest request) {
        if (!isAdmin(request)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        timetableService.deleteSubject(id);
        return ResponseEntity.ok(java.util.Map.of("ok", true));
    }

    @GetMapping("/periods")
    public ResponseEntity<java.util.List<PeriodDto>> listPeriods(
            @RequestParam(value = "classId", required = false) Long classId,
            HttpServletRequest request
    ) {
        if (!isAdmin(request)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return ResponseEntity.ok(timetableService.listPeriods(classId));
    }

    @PostMapping("/periods")
    public ResponseEntity<PeriodDto> createPeriod(
            @RequestBody TimetablePeriodRequest requestBody,
            HttpServletRequest request
    ) {
        if (!isAdmin(request)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return ResponseEntity.ok(timetableService.createPeriod(requestBody));
    }

    @PutMapping("/periods/{id}")
    public ResponseEntity<PeriodDto> updatePeriod(
            @PathVariable Long id,
            @RequestBody TimetablePeriodRequest requestBody,
            HttpServletRequest request
    ) {
        if (!isAdmin(request)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return ResponseEntity.ok(timetableService.updatePeriod(id, requestBody));
    }

    @DeleteMapping("/periods/{id}")
    public ResponseEntity<?> deletePeriod(@PathVariable Long id, HttpServletRequest request) {
        if (!isAdmin(request)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        timetableService.deletePeriod(id);
        return ResponseEntity.ok(java.util.Map.of("ok", true));
    }

    private boolean isAdmin(HttpServletRequest request) {
        Object profileObj = request.getAttribute("userProfile");
        if (profileObj instanceof UserProfile profile) {
            return "admin".equalsIgnoreCase(profile.getRole());
        }
        return false;
    }
}
