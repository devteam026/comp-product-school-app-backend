package com.company.schoolbackend.controller;

import com.company.schoolbackend.dto.FeeSummaryResponse;
import com.company.schoolbackend.dto.PaymentRequest;
import com.company.schoolbackend.dto.PaymentResponse;
import com.company.schoolbackend.dto.ReminderRequest;
import com.company.schoolbackend.dto.ReminderResponse;
import com.company.schoolbackend.service.FeeService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class FeeController {
    private final FeeService feeService;

    public FeeController(FeeService feeService) {
        this.feeService = feeService;
    }

    @PostMapping("/payments")
    public PaymentResponse record(@RequestBody PaymentRequest request) {
        return feeService.recordPayment(request);
    }

    @GetMapping("/payments")
    public List<PaymentResponse> list(
            @RequestParam(value = "studentId", required = false) String studentId,
            @RequestParam(value = "month", required = false) String month
    ) {
        return feeService.listPayments(studentId, month);
    }

    @GetMapping("/fees/summary")
    public FeeSummaryResponse summary(@RequestParam(value = "month", required = false) String month) {
        return feeService.getSummary(month);
    }

    @PostMapping("/fees/reminders")
    public ReminderResponse sendReminders(@RequestBody ReminderRequest request) {
        return feeService.sendReminder(request);
    }
}
