package com.company.schoolbackend.service;

import com.company.schoolbackend.dto.FeeSummaryResponse;
import com.company.schoolbackend.dto.PaymentRequest;
import com.company.schoolbackend.dto.PaymentResponse;
import com.company.schoolbackend.dto.ReminderRequest;
import com.company.schoolbackend.dto.ReminderResponse;
import com.company.schoolbackend.entity.FeeType;
import com.company.schoolbackend.entity.Payment;
import com.company.schoolbackend.entity.PaymentMethod;
import com.company.schoolbackend.entity.Student;
import com.company.schoolbackend.repository.PaymentRepository;
import com.company.schoolbackend.repository.StudentRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FeeService {
    private final PaymentRepository paymentRepository;
    private final StudentRepository studentRepository;

    @Value("${school.monthlyFee:100}")
    private int monthlyFee;

    public FeeService(PaymentRepository paymentRepository, StudentRepository studentRepository) {
        this.paymentRepository = paymentRepository;
        this.studentRepository = studentRepository;
    }

    public PaymentResponse recordPayment(PaymentRequest request) {
        if (request == null || request.getStudentId() == null || request.getStudentId().isBlank()) {
            throw new IllegalArgumentException("Student is required");
        }
        Payment payment = new Payment();
        payment.setId(java.util.UUID.randomUUID().toString());
        payment.setStudentId(request.getStudentId());
        payment.setAmount(new BigDecimal(request.getAmount()));
        payment.setMonths(request.getMonths());
        payment.setMethod(parseMethod(request.getMethod()));
        payment.setNote(request.getNote());
        payment.setPaymentDate(LocalDate.parse(request.getDate()));
        payment.setCreatedAt(OffsetDateTime.now());
        payment.setUpdatedAt(OffsetDateTime.now());

        Payment saved = paymentRepository.save(payment);
        return toResponse(saved);
    }

    public List<PaymentResponse> listPayments(String studentId, String month) {
        List<Payment> payments;
        if (studentId != null && !studentId.isBlank() && month != null && !month.isBlank()) {
            payments = paymentRepository.findByStudentIdAndMonthsContains(studentId, month);
        } else if (studentId != null && !studentId.isBlank()) {
            payments = paymentRepository.findByStudentId(studentId);
        } else if (month != null && !month.isBlank()) {
            payments = paymentRepository.findByMonthsContains(month);
        } else {
            payments = paymentRepository.findAll();
        }
        List<PaymentResponse> responses = new ArrayList<>();
        for (Payment payment : payments) {
            responses.add(toResponse(payment));
        }
        return responses;
    }

    public FeeSummaryResponse getSummary(String month) {
        if (month == null || month.isBlank()) {
            month = LocalDate.now().toString().substring(0, 7);
        }
        List<Student> students = studentRepository.findAll();
        Set<String> paidStudentIds = new HashSet<>();
        BigDecimal paidTotal = BigDecimal.ZERO;

        List<Payment> monthPayments = paymentRepository.findByMonthsContains(month);
        for (Payment payment : monthPayments) {
            paidStudentIds.add(payment.getStudentId());
            paidTotal = paidTotal.add(payment.getAmount());
        }

        int freeCount = 0;
        int paidCount = 0;
        int unpaidCount = 0;
        List<String> unpaidIds = new ArrayList<>();

        for (Student student : students) {
            if (student.getFeeType() == FeeType.Free) {
                freeCount += 1;
                continue;
            }
            if (paidStudentIds.contains(student.getId())) {
                paidCount += 1;
            } else {
                unpaidCount += 1;
                unpaidIds.add(student.getId());
            }
        }

        FeeSummaryResponse response = new FeeSummaryResponse();
        response.setMonth(month);
        response.setMonthlyFee(monthlyFee);
        response.setPaidCount(paidCount);
        response.setUnpaidCount(unpaidCount);
        response.setFreeCount(freeCount);
        response.setPaidTotal(paidTotal.toPlainString());
        response.setUnpaidStudentIds(unpaidIds);
        return response;
    }

    public ReminderResponse sendReminder(ReminderRequest request) {
        if (request == null) {
            return new ReminderResponse(false, 0, "Missing payload");
        }
        int count = 0;
        if ("all".equalsIgnoreCase(request.getScope())) {
            count = studentRepository.findAll().size();
        } else {
            count = request.getStudentIds() == null ? 0 : request.getStudentIds().size();
        }
        return new ReminderResponse(true, count, "Reminder queued for " + count + " parent" + (count == 1 ? "" : "s") + ".");
    }

    private PaymentResponse toResponse(Payment payment) {
        PaymentResponse response = new PaymentResponse();
        response.setId(payment.getId());
        response.setStudentId(payment.getStudentId());
        response.setAmount(payment.getAmount().toPlainString());
        response.setMonths(payment.getMonths());
        response.setMethod(payment.getMethod().name());
        response.setNote(payment.getNote());
        response.setDate(payment.getPaymentDate().toString());
        return response;
    }

    private static PaymentMethod parseMethod(String value) {
        if (value == null || value.isBlank()) {
            return PaymentMethod.Cash;
        }
        for (PaymentMethod method : PaymentMethod.values()) {
            if (method.name().equalsIgnoreCase(value.trim())) {
                return method;
            }
        }
        return PaymentMethod.Cash;
    }
}
