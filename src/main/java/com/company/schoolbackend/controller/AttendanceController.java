package com.company.schoolbackend.controller;

import com.company.schoolbackend.dto.AttendanceListResponse;
import com.company.schoolbackend.dto.AttendanceSaveRequest;
import com.company.schoolbackend.dto.AttendanceSaveResponse;
import com.company.schoolbackend.service.AttendanceService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {
    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @PostMapping
    public AttendanceSaveResponse save(@RequestBody AttendanceSaveRequest request) {
        return attendanceService.save(request);
    }

    @GetMapping
    public AttendanceListResponse list(@RequestParam(value = "date", required = false) String date) {
        return attendanceService.list(date);
    }
}
