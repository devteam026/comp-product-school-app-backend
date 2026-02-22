package com.company.schoolbackend.controller;

import com.company.schoolbackend.dto.DashboardResponse;
import com.company.schoolbackend.service.DashboardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping
    public DashboardResponse getDashboard(
            @RequestParam(value = "classCode", required = false) String classCode
    ) {
        return dashboardService.getDashboard(classCode);
    }
}
