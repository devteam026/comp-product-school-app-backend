package com.company.schoolbackend.service;

import com.company.schoolbackend.dto.FeePaymentDetailResponse;
import com.company.schoolbackend.dto.FeePaymentRequest;
import com.company.schoolbackend.dto.FeePaymentResponse;
import com.company.schoolbackend.entity.DefaultDiscount;
import com.company.schoolbackend.entity.DiscountType;
import com.company.schoolbackend.entity.FeeDue;
import com.company.schoolbackend.entity.FeeDueStatus;
import com.company.schoolbackend.entity.FeeFrequency;
import com.company.schoolbackend.entity.FeePayment;
import com.company.schoolbackend.entity.FeePaymentDetail;
import com.company.schoolbackend.entity.FeeReceipt;
import com.company.schoolbackend.entity.FeeStructure;
import com.company.schoolbackend.entity.FeeTypeEntity;
import com.company.schoolbackend.entity.FineRule;
import com.company.schoolbackend.entity.Student;
import com.company.schoolbackend.entity.StudentDiscount;
import com.company.schoolbackend.entity.StudentStatus;
import com.company.schoolbackend.repository.DefaultDiscountRepository;
import com.company.schoolbackend.repository.FeeDueRepository;
import com.company.schoolbackend.repository.FeePaymentDetailRepository;
import com.company.schoolbackend.repository.FeePaymentRepository;
import com.company.schoolbackend.repository.FeeReceiptRepository;
import com.company.schoolbackend.repository.FeeStructureRepository;
import com.company.schoolbackend.repository.FeeTypeRepository;
import com.company.schoolbackend.repository.FineRuleRepository;
import com.company.schoolbackend.repository.StudentDiscountRepository;
import com.company.schoolbackend.repository.StudentRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FeeManagementService {
    private final FeeTypeRepository feeTypeRepository;
    private final FeeStructureRepository feeStructureRepository;
    private final FeeDueRepository feeDueRepository;
    private final DefaultDiscountRepository defaultDiscountRepository;
    private final StudentDiscountRepository studentDiscountRepository;
    private final FineRuleRepository fineRuleRepository;
    private final FeePaymentRepository feePaymentRepository;
    private final FeePaymentDetailRepository feePaymentDetailRepository;
    private final FeeReceiptRepository feeReceiptRepository;
    private final StudentRepository studentRepository;
    private final com.company.schoolbackend.repository.TransportStoppageRepository transportStoppageRepository;

    public FeeManagementService(
            FeeTypeRepository feeTypeRepository,
            FeeStructureRepository feeStructureRepository,
            FeeDueRepository feeDueRepository,
            DefaultDiscountRepository defaultDiscountRepository,
            StudentDiscountRepository studentDiscountRepository,
            FineRuleRepository fineRuleRepository,
            FeePaymentRepository feePaymentRepository,
            FeePaymentDetailRepository feePaymentDetailRepository,
            FeeReceiptRepository feeReceiptRepository,
            StudentRepository studentRepository,
            com.company.schoolbackend.repository.TransportStoppageRepository transportStoppageRepository
    ) {
        this.feeTypeRepository = feeTypeRepository;
        this.feeStructureRepository = feeStructureRepository;
        this.feeDueRepository = feeDueRepository;
        this.defaultDiscountRepository = defaultDiscountRepository;
        this.studentDiscountRepository = studentDiscountRepository;
        this.fineRuleRepository = fineRuleRepository;
        this.feePaymentRepository = feePaymentRepository;
        this.feePaymentDetailRepository = feePaymentDetailRepository;
        this.feeReceiptRepository = feeReceiptRepository;
        this.studentRepository = studentRepository;
        this.transportStoppageRepository = transportStoppageRepository;
    }

    public List<FeeTypeEntity> listFeeTypes() {
        return feeTypeRepository.findAll();
    }

    public FeeTypeEntity saveFeeType(FeeTypeEntity request) {
        FeeTypeEntity entity = request.getId() == null ? new FeeTypeEntity() : feeTypeRepository.findById(request.getId()).orElse(new FeeTypeEntity());
        entity.setName(request.getName().trim());
        entity.setActive(request.isActive());
        return feeTypeRepository.save(entity);
    }

    public void deleteFeeType(Long id) {
        feeTypeRepository.deleteById(id);
    }

    public List<FeeStructure> listFeeStructures() {
        return feeStructureRepository.findAll();
    }

    public FeeStructure saveFeeStructure(FeeStructure request) {
        FeeStructure entity = request.getId() == null ? new FeeStructure() : feeStructureRepository.findById(request.getId()).orElse(new FeeStructure());
        entity.setClassCode(request.getClassCode());
        entity.setFeeTypeId(request.getFeeTypeId());
        entity.setAmount(request.getAmount());
        entity.setFrequency(request.getFrequency());
        entity.setAcademicYear(request.getAcademicYear());
        entity.setEffectiveFrom(request.getEffectiveFrom());
        entity.setDueDay(request.getDueDay());
        entity.setActive(request.isActive());
        return feeStructureRepository.save(entity);
    }

    public void deleteFeeStructure(Long id) {
        feeStructureRepository.deleteById(id);
    }

    public List<FeeDue> listDues(String studentId, String status) {
        if (studentId != null && !studentId.isBlank()) {
            if (status != null && !status.isBlank()) {
                return feeDueRepository.findByStudentIdAndStatusOrderByDueDateAsc(studentId, FeeDueStatus.valueOf(status.toUpperCase(Locale.ROOT)));
            }
            return feeDueRepository.findByStudentIdOrderByDueDateAsc(studentId);
        }
        if (status != null && !status.isBlank()) {
            return feeDueRepository.findByStatusOrderByDueDateAsc(FeeDueStatus.valueOf(status.toUpperCase(Locale.ROOT)));
        }
        return feeDueRepository.findAll().stream()
                .sorted(Comparator.comparing(FeeDue::getDueDate))
                .collect(Collectors.toList());
    }

    @Transactional
    public Map<String, Object> generateDues(String classCode, String month, String academicYear) {
        if (classCode == null || classCode.isBlank()) {
            throw new IllegalArgumentException("Class is required");
        }
        if (month == null || month.isBlank()) {
            month = LocalDate.now().toString().substring(0, 7);
        }
        List<Student> students = studentRepository.findAll().stream()
                .filter(s -> s.getStatus() == StudentStatus.Active)
                .filter(s -> classCode.equalsIgnoreCase(s.getClassCode()))
                .collect(Collectors.toList());
        List<FeeStructure> allStructures = feeStructureRepository.findByClassCodeAndActiveTrueOrderByFeeTypeIdAsc(classCode);
        Map<Long, FeeTypeEntity> feeTypes = feeTypeRepository.findAll().stream()
                .collect(Collectors.toMap(FeeTypeEntity::getId, item -> item));
        if (academicYear != null && !academicYear.isBlank()) {
            allStructures = allStructures.stream()
                    .filter(s -> academicYear.equalsIgnoreCase(s.getAcademicYear()))
                    .collect(Collectors.toList());
        }
        int totalStructures = (int) allStructures.stream().map(FeeStructure::getFeeTypeId).distinct().count();
        // When multiple structures exist for the same feeTypeId (fee revisions with
        // different effectiveFrom dates), keep only the latest one whose effectiveFrom
        // is on or before the first day of the target month.
        String monthStart = month + "-01";
        List<FeeStructure> structures = resolveApplicableStructures(allStructures, monthStart);
        // Fee types that have structures but are not yet effective for this month
        List<String> notYetEffective = allStructures.stream()
                .map(FeeStructure::getFeeTypeId)
                .distinct()
                .filter(ftId -> structures.stream().noneMatch(s -> s.getFeeTypeId().equals(ftId)))
                .map(ftId -> feeTypes.containsKey(ftId) ? feeTypes.get(ftId).getName() : "Fee #" + ftId)
                .collect(Collectors.toList());
        List<FeeDue> created = new ArrayList<>();
        int skipped = 0;
        for (Student student : students) {
            for (FeeStructure structure : structures) {
                String dueDate = resolveDueDate(structure, month);
                List<FeeDue> existing = feeDueRepository.findByStudentIdAndFeeTypeIdAndDueDate(
                        student.getId(), structure.getFeeTypeId(), dueDate
                );
                if (!existing.isEmpty()) {
                    skipped++;
                    continue;
                }
                BigDecimal amount = resolveDueAmount(structure, student, feeTypes);
                if (amount == null) {
                    continue;
                }
                FeeDue due = new FeeDue();
                due.setStudentId(student.getId());
                due.setFeeTypeId(structure.getFeeTypeId());
                due.setAmount(amount);
                due.setDueDate(dueDate);
                due.setRemainingAmount(amount);
                due.setStatus(FeeDueStatus.UNPAID);
                created.add(feeDueRepository.save(due));
            }
        }
        Map<String, Object> result = new java.util.LinkedHashMap<>();
        result.put("created", created.size());
        result.put("skipped", skipped);
        result.put("notYetEffective", notYetEffective);
        result.put("students", students.size());
        result.put("structures", totalStructures);
        return result;
    }

    @Transactional
    public List<FeeDue> regenerateDues(String classCode, String month, String academicYear) {
        if (classCode == null || classCode.isBlank()) {
            throw new IllegalArgumentException("Class is required");
        }
        if (month == null || month.isBlank()) {
            month = LocalDate.now().toString().substring(0, 7);
        }
        // Block regenerate for past months — only current and future months are allowed
        String currentMonth = LocalDate.now().toString().substring(0, 7); // "YYYY-MM"
        if (month.compareTo(currentMonth) < 0) {
            throw new IllegalArgumentException(
                "Regenerate is not allowed for past months. Selected: " + month + ", Current: " + currentMonth
            );
        }
        List<Student> students = studentRepository.findAll().stream()
                .filter(s -> s.getStatus() == StudentStatus.Active)
                .filter(s -> classCode.equalsIgnoreCase(s.getClassCode()))
                .collect(Collectors.toList());
        List<FeeStructure> structures = feeStructureRepository.findByClassCodeAndActiveTrueOrderByFeeTypeIdAsc(classCode);
        Map<Long, FeeTypeEntity> feeTypes = feeTypeRepository.findAll().stream()
                .collect(Collectors.toMap(FeeTypeEntity::getId, item -> item));
        if (academicYear != null && !academicYear.isBlank()) {
            structures = structures.stream()
                    .filter(s -> academicYear.equalsIgnoreCase(s.getAcademicYear()))
                    .collect(Collectors.toList());
        }
        String monthStartRegen = month + "-01";
        structures = resolveApplicableStructures(structures, monthStartRegen);
        List<FeeDue> updated = new ArrayList<>();
        for (Student student : students) {
            for (FeeStructure structure : structures) {
                String dueDate = resolveDueDate(structure, month);
                List<FeeDue> existing = feeDueRepository.findByStudentIdAndFeeTypeIdAndDueDate(
                        student.getId(), structure.getFeeTypeId(), dueDate
                );
                if (!existing.isEmpty()) {
                    FeeDue due = existing.get(0);
                    if (due.getStatus() != FeeDueStatus.UNPAID) {
                        continue;
                    }
                    BigDecimal amount = resolveDueAmount(structure, student, feeTypes);
                    if (amount == null) {
                        continue;
                    }
                    due.setAmount(amount);
                    due.setRemainingAmount(amount);
                    updated.add(feeDueRepository.save(due));
                    continue;
                }
                BigDecimal amount = resolveDueAmount(structure, student, feeTypes);
                if (amount == null) {
                    continue;
                }
                FeeDue due = new FeeDue();
                due.setStudentId(student.getId());
                due.setFeeTypeId(structure.getFeeTypeId());
                due.setAmount(amount);
                due.setDueDate(dueDate);
                due.setRemainingAmount(amount);
                due.setStatus(FeeDueStatus.UNPAID);
                updated.add(feeDueRepository.save(due));
            }
        }
        return updated;
    }

    public DefaultDiscount saveDefaultDiscount(DefaultDiscount request) {
        DefaultDiscount entity = request.getId() == null ? new DefaultDiscount() : defaultDiscountRepository.findById(request.getId()).orElse(new DefaultDiscount());
        entity.setName(request.getName());
        entity.setDiscountType(request.getDiscountType());
        entity.setValue(request.getValue());
        entity.setApplicableOn(request.getApplicableOn());
        entity.setActive(request.isActive());
        return defaultDiscountRepository.save(entity);
    }

    public void deleteDefaultDiscount(Long id) {
        defaultDiscountRepository.deleteById(id);
    }

    public List<DefaultDiscount> listDefaultDiscounts() {
        return defaultDiscountRepository.findAll();
    }

    public StudentDiscount saveStudentDiscount(StudentDiscount request) {
        StudentDiscount entity = request.getId() == null ? new StudentDiscount() : studentDiscountRepository.findById(request.getId()).orElse(new StudentDiscount());
        entity.setStudentId(request.getStudentId());
        entity.setDiscountId(request.getDiscountId());
        entity.setStartDate(request.getStartDate());
        entity.setEndDate(request.getEndDate());
        entity.setActive(request.isActive());
        return studentDiscountRepository.save(entity);
    }

    public void deleteStudentDiscount(Long id) {
        studentDiscountRepository.deleteById(id);
    }

    public List<StudentDiscount> listStudentDiscounts() {
        return studentDiscountRepository.findAll();
    }

    public FineRule saveFineRule(FineRule request) {
        FineRule entity = request.getId() == null ? new FineRule() : fineRuleRepository.findById(request.getId()).orElse(new FineRule());
        // Check for duplicate fee type — only one fine rule per fee type allowed
        Long currentId = entity.getId();
        boolean duplicate = fineRuleRepository.findAll().stream().anyMatch(r ->
                r.getFeeTypeId().equals(request.getFeeTypeId()) && (currentId == null || !r.getId().equals(currentId)));
        if (duplicate) {
            throw new IllegalArgumentException("A fine rule for this fee type already exists.");
        }
        entity.setFeeTypeId(request.getFeeTypeId());
        entity.setFrequency(request.getFrequency());
        entity.setValue(request.getValue());
        entity.setActive(request.isActive());
        return fineRuleRepository.save(entity);
    }

    public void deleteFineRule(Long id) {
        fineRuleRepository.deleteById(id);
    }

    public List<FineRule> listFineRules() {
        return fineRuleRepository.findAll();
    }

    @Transactional
    public FeePaymentResponse recordPayment(FeePaymentRequest request) {
        if (request == null || request.getStudentId() == null || request.getStudentId().isBlank()) {
            throw new IllegalArgumentException("Student is required");
        }
        BigDecimal paidAmount = parseAmount(request.getPaidAmount());
        BigDecimal extraDiscount = parseAmount(request.getExtraDiscount());
        String paymentDate = request.getPaymentDate() == null || request.getPaymentDate().isBlank()
                ? LocalDate.now().toString()
                : request.getPaymentDate();

        List<FeeDue> dues = feeDueRepository.findByStudentIdOrderByDueDateAsc(request.getStudentId()).stream()
                .filter(due -> due.getStatus() != FeeDueStatus.PAID)
                .collect(Collectors.toList());
        if (dues.isEmpty()) {
            throw new IllegalArgumentException("No outstanding dues");
        }

        Map<Long, FeeTypeEntity> feeTypes = feeTypeRepository.findAll().stream()
                .collect(Collectors.toMap(FeeTypeEntity::getId, item -> item));
        List<DefaultDiscount> defaultDiscounts = defaultDiscountRepository.findByActiveTrueOrderByNameAsc();
        List<StudentDiscount> studentDiscounts = studentDiscountRepository.findByStudentId(request.getStudentId())
                .stream().filter(StudentDiscount::isActive).collect(Collectors.toList());
        List<FineRule> fineRules = fineRuleRepository.findByActiveTrueOrderByIdAsc();

        List<FeePaymentDetail> details = new ArrayList<>();
        BigDecimal totalDue = BigDecimal.ZERO;
        BigDecimal totalDefaultDiscount = BigDecimal.ZERO;
        BigDecimal totalExtraDiscount = BigDecimal.ZERO;
        BigDecimal totalFine = BigDecimal.ZERO;
        BigDecimal finalAmount = BigDecimal.ZERO;
        BigDecimal remainingPayment = paidAmount;
        BigDecimal remainingExtraDiscount = extraDiscount;

        for (FeeDue due : dues) {
            BigDecimal dueAmount = due.getRemainingAmount();
            BigDecimal defaultDisc = calculateDiscount(due, feeTypes, defaultDiscounts, studentDiscounts, paymentDate);
            BigDecimal fine = request.isWaiveFine() ? BigDecimal.ZERO : calculateFine(due, fineRules, paymentDate);
            BigDecimal extraDisc = BigDecimal.ZERO;
            if (remainingExtraDiscount.compareTo(BigDecimal.ZERO) > 0) {
                extraDisc = remainingExtraDiscount.min(dueAmount.subtract(defaultDisc).max(BigDecimal.ZERO));
                remainingExtraDiscount = remainingExtraDiscount.subtract(extraDisc);
            }
            BigDecimal dueFinal = dueAmount.subtract(defaultDisc).subtract(extraDisc).add(fine);
            if (dueFinal.compareTo(BigDecimal.ZERO) < 0) {
                dueFinal = BigDecimal.ZERO;
            }
            BigDecimal payApplied = remainingPayment.min(dueFinal);
            remainingPayment = remainingPayment.subtract(payApplied);

            FeePaymentDetail detail = new FeePaymentDetail();
            detail.setDueId(due.getId());
            detail.setDueAmount(dueAmount);
            detail.setDefaultDiscount(defaultDisc);
            detail.setExtraDiscount(extraDisc);
            detail.setFineAmount(fine);
            detail.setFinalAmount(dueFinal);
            detail.setPaidAmount(payApplied);
            detail.setExtraDiscountReason(request.getExtraDiscountReason());
            detail.setApprovedBy(request.getApprovedBy());
            details.add(detail);

            totalDue = totalDue.add(dueAmount);
            totalDefaultDiscount = totalDefaultDiscount.add(defaultDisc);
            totalExtraDiscount = totalExtraDiscount.add(extraDisc);
            totalFine = totalFine.add(fine);
            finalAmount = finalAmount.add(dueFinal);

            BigDecimal newRemaining = dueFinal.subtract(payApplied).max(BigDecimal.ZERO);
            if (newRemaining.compareTo(BigDecimal.ZERO) == 0) {
                due.setRemainingAmount(BigDecimal.ZERO);
                due.setStatus(FeeDueStatus.PAID);
            } else if (newRemaining.compareTo(dueAmount) < 0) {
                due.setRemainingAmount(newRemaining);
                due.setStatus(FeeDueStatus.PARTIAL);
            } else {
                due.setRemainingAmount(newRemaining);
                due.setStatus(FeeDueStatus.UNPAID);
            }
            feeDueRepository.save(due);

            if (remainingPayment.compareTo(BigDecimal.ZERO) <= 0) {
                break;
            }
        }

        FeePayment payment = new FeePayment();
        payment.setStudentId(request.getStudentId());
        payment.setTotalDue(totalDue);
        payment.setTotalDefaultDiscount(totalDefaultDiscount);
        payment.setTotalExtraDiscount(totalExtraDiscount);
        payment.setTotalFine(totalFine);
        payment.setFinalAmount(finalAmount);
        payment.setPaidAmount(paidAmount);
        payment.setPaymentDate(paymentDate);
        payment.setPaymentMode(defaultIfBlank(request.getPaymentMode(), "Cash"));
        payment.setCreatedAt(OffsetDateTime.now().toString());

        FeePayment saved = feePaymentRepository.save(payment);
        for (FeePaymentDetail detail : details) {
            detail.setPaymentId(saved.getId());
            feePaymentDetailRepository.save(detail);
        }
        FeeReceipt receipt = new FeeReceipt();
        receipt.setPaymentId(saved.getId());
        receipt.setReceiptDate(paymentDate);
        receipt.setReceiptNumber("RCPT-" + saved.getId());
        feeReceiptRepository.save(receipt);

        return buildResponse(saved, details, receipt);
    }

    public List<FeePaymentResponse> listPayments(String studentId) {
        List<FeePayment> payments = studentId == null || studentId.isBlank()
                ? feePaymentRepository.findAll()
                : feePaymentRepository.findByStudentIdOrderByPaymentDateDesc(studentId);
        List<FeePaymentResponse> responses = new ArrayList<>();
        for (FeePayment payment : payments) {
            List<FeePaymentDetail> details = feePaymentDetailRepository.findByPaymentId(payment.getId());
            List<FeeReceipt> receipts = feeReceiptRepository.findByPaymentId(payment.getId());
            FeeReceipt receipt = receipts.isEmpty() ? null : receipts.get(0);
            responses.add(buildResponse(payment, details, receipt));
        }
        return responses;
    }

    private FeePaymentResponse buildResponse(FeePayment payment, List<FeePaymentDetail> details, FeeReceipt receipt) {
        FeePaymentResponse response = new FeePaymentResponse();
        response.setId(payment.getId());
        response.setStudentId(payment.getStudentId());
        response.setTotalDue(payment.getTotalDue().toPlainString());
        response.setTotalDefaultDiscount(payment.getTotalDefaultDiscount().toPlainString());
        response.setTotalExtraDiscount(payment.getTotalExtraDiscount().toPlainString());
        response.setTotalFine(payment.getTotalFine().toPlainString());
        response.setFinalAmount(payment.getFinalAmount().toPlainString());
        response.setPaidAmount(payment.getPaidAmount().toPlainString());
        response.setPaymentDate(payment.getPaymentDate());
        response.setPaymentMode(payment.getPaymentMode());
        response.setReceiptNumber(receipt == null ? null : receipt.getReceiptNumber());
        List<FeePaymentDetailResponse> detailResponses = new ArrayList<>();
        for (FeePaymentDetail detail : details) {
            FeePaymentDetailResponse d = new FeePaymentDetailResponse();
            d.setId(detail.getId());
            d.setDueId(detail.getDueId());
            d.setDueAmount(detail.getDueAmount().toPlainString());
            d.setDefaultDiscount(detail.getDefaultDiscount().toPlainString());
            d.setExtraDiscount(detail.getExtraDiscount().toPlainString());
            d.setFineAmount(detail.getFineAmount().toPlainString());
            d.setFinalAmount(detail.getFinalAmount().toPlainString());
            d.setPaidAmount(detail.getPaidAmount().toPlainString());
            d.setExtraDiscountReason(detail.getExtraDiscountReason());
            d.setApprovedBy(detail.getApprovedBy());
            detailResponses.add(d);
        }
        response.setDetails(detailResponses);
        return response;
    }

    private BigDecimal calculateDiscount(
            FeeDue due,
            Map<Long, FeeTypeEntity> feeTypes,
            List<DefaultDiscount> defaultDiscounts,
            List<StudentDiscount> studentDiscounts,
            String paymentDate
    ) {
        BigDecimal discount = BigDecimal.ZERO;
        Long feeTypeId = due.getFeeTypeId();
        for (DefaultDiscount def : defaultDiscounts) {
            if (!def.isActive()) continue;
            if (!isDiscountApplicable(def.getApplicableOn(), feeTypeId, feeTypes)) continue;
            discount = discount.add(applyDiscount(def.getDiscountType(), def.getValue(), due.getRemainingAmount()));
        }
        for (StudentDiscount sd : studentDiscounts) {
            if (!sd.isActive()) continue;
            if (!isDateBetween(paymentDate, sd.getStartDate(), sd.getEndDate())) continue;
            DefaultDiscount def = defaultDiscountRepository.findById(sd.getDiscountId()).orElse(null);
            if (def == null) continue;
            if (!isDiscountApplicable(def.getApplicableOn(), feeTypeId, feeTypes)) continue;
            discount = discount.add(applyDiscount(def.getDiscountType(), def.getValue(), due.getRemainingAmount()));
        }
        if (discount.compareTo(due.getRemainingAmount()) > 0) {
            return due.getRemainingAmount();
        }
        return discount;
    }

    private BigDecimal calculateFine(FeeDue due, List<FineRule> rules, String paymentDate) {
        if (rules == null || rules.isEmpty()) return BigDecimal.ZERO;
        LocalDate dueDate = LocalDate.parse(due.getDueDate());
        LocalDate payDate = LocalDate.parse(paymentDate);
        if (!payDate.isAfter(dueDate)) return BigDecimal.ZERO;
        long daysLate = java.time.temporal.ChronoUnit.DAYS.between(dueDate, payDate);
        BigDecimal totalFine = BigDecimal.ZERO;
        // If a specific rule exists for this fee type, skip "All Fee Types" rules
        boolean hasSpecificRule = rules.stream().anyMatch(r ->
                r.isActive() && r.getFeeTypeId() != 0 && r.getFeeTypeId().equals(due.getFeeTypeId()));
        for (FineRule rule : rules) {
            if (!rule.isActive()) continue;
            if (rule.getFeeTypeId() != 0 && !rule.getFeeTypeId().equals(due.getFeeTypeId())) continue;
            if (rule.getFeeTypeId() == 0 && hasSpecificRule) continue;
            long periods;
            switch (rule.getFrequency()) {
                case DAILY:
                    periods = daysLate;
                    break;
                case WEEKLY:
                    periods = (daysLate + 6) / 7;
                    break;
                case MONTHLY:
                    periods = java.time.temporal.ChronoUnit.MONTHS.between(dueDate, payDate);
                    if (periods < 1) periods = 1;
                    break;
                default:
                    periods = 0;
            }
            totalFine = totalFine.add(rule.getValue().multiply(BigDecimal.valueOf(periods)));
        }
        return totalFine;
    }

    private static boolean isDiscountApplicable(String applicableOn, Long feeTypeId, Map<Long, FeeTypeEntity> feeTypes) {
        if (applicableOn == null || "ALL".equalsIgnoreCase(applicableOn)) return true;
        // Support legacy enum values (TUITION, TRANSPORT) by matching fee type name
        if ("TUITION".equalsIgnoreCase(applicableOn)) {
            FeeTypeEntity ft = feeTypes.get(feeTypeId);
            return ft != null && ft.getName().toLowerCase(Locale.ROOT).contains("tuition");
        }
        if ("TRANSPORT".equalsIgnoreCase(applicableOn)) {
            FeeTypeEntity ft = feeTypes.get(feeTypeId);
            return ft != null && ft.getName().toLowerCase(Locale.ROOT).contains("transport");
        }
        try {
            return Long.parseLong(applicableOn) == feeTypeId;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static BigDecimal applyDiscount(DiscountType type, BigDecimal value, BigDecimal base) {
        if (value == null) return BigDecimal.ZERO;
        if (type == DiscountType.PERCENTAGE) {
            return base.multiply(value).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        }
        return value;
    }

    /**
     * For each feeTypeId, keep only the structure with the latest effectiveFrom
     * that is on or before the given cutoff date (e.g. "2026-05-01").
     * This ensures fee revisions supersede earlier structures.
     */
    private static List<FeeStructure> resolveApplicableStructures(List<FeeStructure> structures, String cutoffDate) {
        Map<Long, FeeStructure> best = new java.util.LinkedHashMap<>();
        for (FeeStructure s : structures) {
            String ef = s.getEffectiveFrom();
            if (ef == null || ef.compareTo(cutoffDate) > 0) {
                // effectiveFrom is after the cutoff — not yet applicable
                continue;
            }
            FeeStructure current = best.get(s.getFeeTypeId());
            if (current == null || ef.compareTo(current.getEffectiveFrom()) > 0) {
                best.put(s.getFeeTypeId(), s);
            }
        }
        return new ArrayList<>(best.values());
    }

    private static String resolveDueDate(FeeStructure structure, String month) {
        Integer dueDay = structure.getDueDay();
        if (dueDay == null || dueDay < 1 || dueDay > 28) {
            dueDay = 1;
        }
        if (structure.getFrequency() == FeeFrequency.MONTHLY) {
            // Monthly: due every month on the specified day
            return month + "-" + String.format("%02d", dueDay);
        }
        String effective = structure.getEffectiveFrom();
        if (structure.getFrequency() == FeeFrequency.YEARLY && effective != null && effective.length() >= 7) {
            // Yearly: use the month from effectiveFrom but the year from the selected month
            // e.g. effectiveFrom=2026-04-01, month=2027-04 → dueDate=2027-04-{dueDay}
            String selectedYear = month.substring(0, 4);
            String effectiveMonth = effective.substring(5, 7);
            return selectedYear + "-" + effectiveMonth + "-" + String.format("%02d", dueDay);
        }
        // ONE_TIME: due date is fixed to effectiveFrom date
        if (effective != null && effective.length() >= 10) {
            return effective.substring(0, 8) + String.format("%02d", dueDay);
        }
        return month + "-" + String.format("%02d", dueDay);
    }

    private BigDecimal resolveDueAmount(FeeStructure structure, Student student, Map<Long, FeeTypeEntity> feeTypes) {
        FeeTypeEntity feeType = feeTypes.get(structure.getFeeTypeId());
        String feeName = feeType == null ? "" : feeType.getName().toLowerCase(Locale.ROOT);
        if (feeName.contains("transport")) {
            if (!Boolean.TRUE.equals(student.getTransportRequired())) {
                return null;
            }
            String stop = student.getTransportStopName();
            if (stop != null) {
                var stoppage = transportStoppageRepository
                        .findFirstByRouteNameIgnoreCaseAndStopNameIgnoreCase(
                                student.getTransportRoute() == null ? "" : student.getTransportRoute(),
                                stop
                        );
                if (stoppage == null) {
                    stoppage = transportStoppageRepository
                            .findFirstByRouteNameIgnoreCaseAndStopNameIgnoreCase("", stop);
                }
                if (stoppage == null) {
                    stoppage = transportStoppageRepository
                            .findFirstByRouteNameIgnoreCaseAndStopNameIgnoreCase("all", stop);
                }
                if (stoppage == null) {
                    stoppage = transportStoppageRepository
                            .findFirstByRouteNameIgnoreCaseAndStopNameIgnoreCase("default", stop);
                }
                if (stoppage == null) {
                    stoppage = transportStoppageRepository
                            .findFirstByStopNameIgnoreCase(stop);
                }
                if (stoppage != null && stoppage.getFeeAmount() != null) {
                    return stoppage.getFeeAmount();
                }
            }
        }
        return structure.getAmount();
    }

    private static BigDecimal parseAmount(String value) {
        if (value == null || value.isBlank()) return BigDecimal.ZERO;
        return new BigDecimal(value.trim());
    }

    private static String defaultIfBlank(String value, String fallback) {
        return value == null || value.isBlank() ? fallback : value.trim();
    }

    private static boolean isDateBetween(String date, String start, String end) {
        try {
            LocalDate value = LocalDate.parse(date);
            LocalDate s = LocalDate.parse(start);
            LocalDate e = LocalDate.parse(end);
            return (value.isEqual(s) || value.isAfter(s)) && (value.isEqual(e) || value.isBefore(e));
        } catch (Exception ex) {
            return false;
        }
    }
}
