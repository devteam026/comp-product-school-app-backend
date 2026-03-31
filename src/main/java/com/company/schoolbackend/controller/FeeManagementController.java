package com.company.schoolbackend.controller;

import com.company.schoolbackend.dto.FeePaymentRequest;
import com.company.schoolbackend.dto.FeePaymentResponse;
import com.company.schoolbackend.entity.DefaultDiscount;
import com.company.schoolbackend.entity.FeeDue;
import com.company.schoolbackend.entity.FeeStructure;
import com.company.schoolbackend.entity.FeeTypeEntity;
import com.company.schoolbackend.entity.FineRule;
import com.company.schoolbackend.entity.StudentDiscount;
import com.company.schoolbackend.service.FeeManagementService;
import java.util.List;
import java.util.Map;
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
@RequestMapping("/api/fees")
public class FeeManagementController {
    private final FeeManagementService feeManagementService;

    public FeeManagementController(FeeManagementService feeManagementService) {
        this.feeManagementService = feeManagementService;
    }

    @GetMapping("/types")
    public List<FeeTypeEntity> listTypes() {
        return feeManagementService.listFeeTypes();
    }

    @PostMapping("/types")
    public FeeTypeEntity createType(@RequestBody FeeTypeEntity request) {
        return feeManagementService.saveFeeType(request);
    }

    @PutMapping("/types/{id}")
    public FeeTypeEntity updateType(@PathVariable Long id, @RequestBody FeeTypeEntity request) {
        request.setId(id);
        return feeManagementService.saveFeeType(request);
    }

    @DeleteMapping("/types/{id}")
    public ResponseEntity<?> deleteType(@PathVariable Long id) {
        feeManagementService.deleteFeeType(id);
        return ResponseEntity.ok(Map.of("ok", true));
    }

    @GetMapping("/structures")
    public List<FeeStructure> listStructures() {
        return feeManagementService.listFeeStructures();
    }

    @PostMapping("/structures")
    public FeeStructure createStructure(@RequestBody FeeStructure request) {
        return feeManagementService.saveFeeStructure(request);
    }

    @PutMapping("/structures/{id}")
    public FeeStructure updateStructure(@PathVariable Long id, @RequestBody FeeStructure request) {
        request.setId(id);
        return feeManagementService.saveFeeStructure(request);
    }

    @DeleteMapping("/structures/{id}")
    public ResponseEntity<?> deleteStructure(@PathVariable Long id) {
        feeManagementService.deleteFeeStructure(id);
        return ResponseEntity.ok(Map.of("ok", true));
    }

    @GetMapping("/dues")
    public List<FeeDue> listDues(
            @RequestParam(value = "studentId", required = false) String studentId,
            @RequestParam(value = "status", required = false) String status
    ) {
        return feeManagementService.listDues(studentId, status);
    }

    @PostMapping("/dues/generate")
    public List<FeeDue> generateDues(@RequestBody Map<String, String> request) {
        String classCode = request.get("classCode");
        String month = request.get("month");
        String academicYear = request.get("academicYear");
        return feeManagementService.generateDues(classCode, month, academicYear);
    }

    @PostMapping("/dues/regenerate")
    public List<FeeDue> regenerateDues(@RequestBody Map<String, String> request) {
        String classCode = request.get("classCode");
        String month = request.get("month");
        String academicYear = request.get("academicYear");
        return feeManagementService.regenerateDues(classCode, month, academicYear);
    }

    @GetMapping("/discounts/default")
    public List<DefaultDiscount> listDefaultDiscounts() {
        return feeManagementService.listDefaultDiscounts();
    }

    @PostMapping("/discounts/default")
    public DefaultDiscount createDefaultDiscount(@RequestBody DefaultDiscount request) {
        return feeManagementService.saveDefaultDiscount(request);
    }

    @PutMapping("/discounts/default/{id}")
    public DefaultDiscount updateDefaultDiscount(@PathVariable Long id, @RequestBody DefaultDiscount request) {
        request.setId(id);
        return feeManagementService.saveDefaultDiscount(request);
    }

    @DeleteMapping("/discounts/default/{id}")
    public ResponseEntity<?> deleteDefaultDiscount(@PathVariable Long id) {
        feeManagementService.deleteDefaultDiscount(id);
        return ResponseEntity.ok(Map.of("ok", true));
    }

    @GetMapping("/discounts/student")
    public List<StudentDiscount> listStudentDiscounts() {
        return feeManagementService.listStudentDiscounts();
    }

    @PostMapping("/discounts/student")
    public StudentDiscount createStudentDiscount(@RequestBody StudentDiscount request) {
        return feeManagementService.saveStudentDiscount(request);
    }

    @PutMapping("/discounts/student/{id}")
    public StudentDiscount updateStudentDiscount(@PathVariable Long id, @RequestBody StudentDiscount request) {
        request.setId(id);
        return feeManagementService.saveStudentDiscount(request);
    }

    @DeleteMapping("/discounts/student/{id}")
    public ResponseEntity<?> deleteStudentDiscount(@PathVariable Long id) {
        feeManagementService.deleteStudentDiscount(id);
        return ResponseEntity.ok(Map.of("ok", true));
    }

    @GetMapping("/fines")
    public List<FineRule> listFineRules() {
        return feeManagementService.listFineRules();
    }

    @PostMapping("/fines")
    public FineRule createFineRule(@RequestBody FineRule request) {
        return feeManagementService.saveFineRule(request);
    }

    @PutMapping("/fines/{id}")
    public FineRule updateFineRule(@PathVariable Long id, @RequestBody FineRule request) {
        request.setId(id);
        return feeManagementService.saveFineRule(request);
    }

    @DeleteMapping("/fines/{id}")
    public ResponseEntity<?> deleteFineRule(@PathVariable Long id) {
        feeManagementService.deleteFineRule(id);
        return ResponseEntity.ok(Map.of("ok", true));
    }

    @PostMapping("/payments")
    public FeePaymentResponse recordPayment(@RequestBody FeePaymentRequest request) {
        return feeManagementService.recordPayment(request);
    }

    @GetMapping("/payments")
    public List<FeePaymentResponse> listPayments(
            @RequestParam(value = "studentId", required = false) String studentId
    ) {
        return feeManagementService.listPayments(studentId);
    }
}
