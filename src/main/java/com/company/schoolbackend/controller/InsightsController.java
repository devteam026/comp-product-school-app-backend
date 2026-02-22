package com.company.schoolbackend.controller;

import com.company.schoolbackend.dto.InsightsResponse;
import com.company.schoolbackend.service.InsightsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/insights")
public class InsightsController {
    private final InsightsService insightsService;

    public InsightsController(InsightsService insightsService) {
        this.insightsService = insightsService;
    }

    @GetMapping
    public InsightsResponse getInsights(
            @RequestParam(value = "classCode", required = false) String classCode,
            @RequestParam(value = "scoreOp", required = false) String scoreOp,
            @RequestParam(value = "scoreValue", required = false) Double scoreValue
    ) {
        return insightsService.getInsights(classCode, scoreOp, scoreValue);
    }
}
