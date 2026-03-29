package com.company.schoolbackend.service;

import com.company.schoolbackend.dto.GuardianInfo;
import com.company.schoolbackend.dto.StudentResponse;
import com.company.schoolbackend.dto.StudentUpsertRequest;
import com.company.schoolbackend.entity.AppUser;
import com.company.schoolbackend.entity.FeeType;
import com.company.schoolbackend.entity.Gender;
import com.company.schoolbackend.entity.Student;
import com.company.schoolbackend.entity.StudentAdmissionSequence;
import com.company.schoolbackend.entity.StudentHistory;
import com.company.schoolbackend.entity.StudentStatus;
import com.company.schoolbackend.entity.UserRole;
import com.company.schoolbackend.repository.AppUserRepository;
import com.company.schoolbackend.repository.StudentHistoryRepository;
import com.company.schoolbackend.repository.StudentRepository;
import com.company.schoolbackend.repository.StudentAdmissionSequenceRepository;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final StudentHistoryRepository historyRepository;
    private final StudentAdmissionSequenceRepository admissionSequenceRepository;
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    public StudentService(
            StudentRepository studentRepository,
            StudentHistoryRepository historyRepository,
            StudentAdmissionSequenceRepository admissionSequenceRepository,
            AppUserRepository appUserRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.studentRepository = studentRepository;
        this.historyRepository = historyRepository;
        this.admissionSequenceRepository = admissionSequenceRepository;
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<StudentResponse> list(String classCode, String gender, String status, String q) {
        String query = q == null || q.isBlank() ? null : "%" + q.trim().toLowerCase(Locale.ROOT) + "%";
        Gender genderEnum = parseEnum(Gender.class, gender);
        StudentStatus statusEnum = parseEnum(StudentStatus.class, status);
        String code = classCode == null || classCode.equalsIgnoreCase("all") ? null : classCode;

        List<Student> students = studentRepository.search(code, genderEnum, statusEnum, query);
        List<StudentResponse> responses = new ArrayList<>();
        for (Student student : students) {
            responses.add(toResponse(student, historyRepository.findByStudentIdOrderByCreatedAtDesc(student.getId())));
        }
        return responses;
    }

    public StudentResponse get(String id) {
        Student student = studentRepository.findById(id).orElseThrow();
        List<StudentHistory> history = historyRepository.findByStudentIdOrderByCreatedAtDesc(id);
        return toResponse(student, history);
    }

    public List<GuardianInfo> searchGuardians(String q) {
        String query = q == null || q.isBlank() ? null : "%" + q.trim().toLowerCase(Locale.ROOT) + "%";
        return studentRepository.searchGuardians(query).stream()
                .filter(item -> item.getParentName() != null && !item.getParentName().isBlank())
                .collect(Collectors.toList());
    }

    @Transactional
    public StudentResponse upsert(StudentUpsertRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Invalid request");
        }
        if (isBlank(request.getName())) {
            throw new IllegalArgumentException("Student name is required");
        }
        if (isBlank(request.getGrade())) {
            throw new IllegalArgumentException("Class is required");
        }
        if (isBlank(request.getSession())) {
            throw new IllegalArgumentException("Session is required");
        }
        if (isBlank(request.getFatherName())) {
            throw new IllegalArgumentException("Father name is required");
        }
        if (isBlank(request.getMotherName())) {
            throw new IllegalArgumentException("Mother name is required");
        }
        if (isBlank(request.getParentName())) {
            throw new IllegalArgumentException("Parent/Guardian name is required");
        }
        if (isBlank(request.getParentRelation())) {
            throw new IllegalArgumentException("Parent relation is required");
        }
        if (isBlank(request.getParentPhone())) {
            throw new IllegalArgumentException("Parent mobile number is required");
        }
        if (isBlank(request.getParentWhatsapp())) {
            throw new IllegalArgumentException("Parent WhatsApp number is required");
        }
        if (isBlank(request.getParentEmail())) {
            throw new IllegalArgumentException("Parent email is required");
        }
        if (isBlank(request.getParentOccupation())) {
            throw new IllegalArgumentException("Parent occupation is required");
        }
        if (request.getTransportRequired() != null && request.getTransportRequired()) {
            if (isBlank(request.getTransportRoute())) {
                throw new IllegalArgumentException("Transport route is required");
            }
            if (isBlank(request.getTransportVehicleNo())) {
                throw new IllegalArgumentException("Vehicle number is required");
            }
        }
        if (Boolean.TRUE.equals(request.getHostelRequired())) {
            if (isBlank(request.getHostelName())) {
                throw new IllegalArgumentException("Hostel name is required");
            }
            if (isBlank(request.getHostelRoomNo())) {
                throw new IllegalArgumentException("Hostel room number is required");
            }
        }
        if (isBlank(request.getRollNumber())) {
            throw new IllegalArgumentException("Roll number is required");
        }
        if (isBlank(request.getRegisterNo())) {
            throw new IllegalArgumentException("Register number is required");
        }
        if (isBlank(request.getAddress())) {
            throw new IllegalArgumentException("Address is required");
        }
        String id = request.getId();
        boolean isNew = id == null || id.isBlank();
        Student student = isNew ? new Student() : studentRepository.findById(id).orElse(new Student());
        if (isNew) {
            student.setId(java.util.UUID.randomUUID().toString());
            student.setCreatedAt(OffsetDateTime.now());
        }
        if (!isNew && student.getCreatedAt() == null) {
            student.setCreatedAt(OffsetDateTime.now());
        }

        student.setName(request.getName().trim());
        String grade = request.getGrade() == null || request.getGrade().isBlank() ? "-" : request.getGrade().trim();
        String section = request.getSection() == null || request.getSection().isBlank() ? "N/A" : request.getSection().trim();
        student.setGrade(grade);
        student.setSection(section);
        student.setClassCode("N/A".equalsIgnoreCase(section) ? grade : grade + section);
        Gender genderValue = parseEnum(Gender.class, request.getGender());
        student.setGender(genderValue == null ? Gender.Male : genderValue);
        student.setDateOfBirth(parseDate(request.getDateOfBirth()));
        if (isBlank(request.getAdmissionNumber())) {
            StudentAdmissionSequence seq = admissionSequenceRepository.save(new StudentAdmissionSequence());
            student.setAdmissionNumber(String.valueOf(seq.getId()));
        } else {
            student.setAdmissionNumber(trim(request.getAdmissionNumber()));
        }
        student.setRegisterNo(trim(request.getRegisterNo()));
        student.setRollNumber(trim(request.getRollNumber()));
        student.setAddress(trim(request.getAddress()));
        student.setSession(trim(request.getSession()));
        student.setFatherName(trim(request.getFatherName()));
        student.setMotherName(trim(request.getMotherName()));
        student.setParentName(trim(request.getParentName()));
        student.setParentRelation(trim(request.getParentRelation()));
        student.setParentPhone(trim(request.getParentPhone()));
        student.setParentWhatsapp(trim(request.getParentWhatsapp()));
        student.setParentEmail(trim(request.getParentEmail()));
        student.setParentOccupation(trim(request.getParentOccupation()));
        student.setTransportRequired(Boolean.TRUE.equals(request.getTransportRequired()));
        student.setTransportRoute(trim(request.getTransportRoute()));
        student.setTransportVehicleNo(trim(request.getTransportVehicleNo()));
        boolean hasHostelPayload =
                request.getHostelRequired() != null
                        || request.getHostelName() != null
                        || request.getHostelRoomNo() != null;
        if (hasHostelPayload) {
            student.setHostelRequired(Boolean.TRUE.equals(request.getHostelRequired()));
            student.setHostelName(trim(request.getHostelName()));
            student.setHostelRoomNo(trim(request.getHostelRoomNo()));
        } else if (isNew) {
            student.setHostelRequired(false);
            student.setHostelName(null);
            student.setHostelRoomNo(null);
        }
        student.setPreviousSchoolName(trim(request.getPreviousSchoolName()));
        student.setPreviousQualification(trim(request.getPreviousQualification()));
        StudentStatus statusValue = parseEnum(StudentStatus.class, request.getStatus());
        FeeType feeTypeValue = parseEnum(FeeType.class, request.getFeeType());
        student.setStatus(statusValue == null ? StudentStatus.Active : statusValue);
        student.setFeeType(feeTypeValue == null ? FeeType.Paid : feeTypeValue);
        if (request.getProfilePhotoKey() != null && !request.getProfilePhotoKey().isBlank()) {
            student.setProfilePhotoKey(request.getProfilePhotoKey().trim());
        }
        student.setUpdatedAt(OffsetDateTime.now());

        Student saved = studentRepository.save(student);

        if (!isBlank(request.getStudentPassword())) {
            String username = saved.getParentPhone().trim().toLowerCase(Locale.ROOT);
            AppUser existingUser = appUserRepository.findByUsername(username).orElse(null);
            if (existingUser != null && existingUser.getRole() != UserRole.student) {
                throw new IllegalArgumentException("Username already used for another role");
            }
            AppUser user = existingUser == null ? new AppUser() : existingUser;
            if (user.getId() == null) {
                user.setUsername(username);
                user.setCreatedAt(OffsetDateTime.now());
            }
            user.setDisplayName(saved.getName());
            user.setRole(UserRole.student);
            user.setActive(true);
            user.setPasswordHash(passwordEncoder.encode(request.getStudentPassword()));
            appUserRepository.save(user);
        }

        StudentHistory history = new StudentHistory();
        history.setStudentId(saved.getId());
        history.setEntry(isNew ? "Student record created" : "Student record updated");
        history.setCreatedAt(OffsetDateTime.now());
        historyRepository.save(history);

        List<StudentHistory> historyList = historyRepository.findByStudentIdOrderByCreatedAtDesc(saved.getId());
        return toResponse(saved, historyList);
    }

    public void delete(String id) {
        studentRepository.deleteById(id);
    }

    private StudentResponse toResponse(Student student, List<StudentHistory> history) {
        StudentResponse response = new StudentResponse();
        response.setId(student.getId());
        response.setName(student.getName());
        response.setGrade(student.getGrade());
        response.setSection(student.getSection());
        response.setClassCode(student.getClassCode());
        response.setGender(student.getGender().name());
        response.setDateOfBirth(student.getDateOfBirth() == null ? "" : student.getDateOfBirth().toString());
        response.setAdmissionNumber(student.getAdmissionNumber());
        response.setRegisterNo(student.getRegisterNo());
        response.setRollNumber(student.getRollNumber());
        response.setAddress(student.getAddress());
        response.setSession(student.getSession());
        response.setFatherName(student.getFatherName());
        response.setMotherName(student.getMotherName());
        response.setParentName(student.getParentName());
        response.setParentRelation(student.getParentRelation());
        response.setParentPhone(student.getParentPhone());
        response.setParentWhatsapp(student.getParentWhatsapp());
        response.setParentEmail(student.getParentEmail());
        response.setParentOccupation(student.getParentOccupation());
        response.setTransportRequired(Boolean.TRUE.equals(student.getTransportRequired()));
        response.setTransportRoute(student.getTransportRoute());
        response.setTransportVehicleNo(student.getTransportVehicleNo());
        response.setHostelRequired(Boolean.TRUE.equals(student.getHostelRequired()));
        response.setHostelName(student.getHostelName());
        response.setHostelRoomNo(student.getHostelRoomNo());
        response.setPreviousSchoolName(student.getPreviousSchoolName());
        response.setPreviousQualification(student.getPreviousQualification());
        response.setStatus(student.getStatus().name());
        response.setFeeType(student.getFeeType().name());
        response.setProfilePhotoKey(student.getProfilePhotoKey());
        List<String> entries = new ArrayList<>();
        List<com.company.schoolbackend.dto.StudentHistoryEntry> historyEntries = new ArrayList<>();
        for (StudentHistory item : history) {
            entries.add(item.getEntry());
            com.company.schoolbackend.dto.StudentHistoryEntry historyEntry =
                    new com.company.schoolbackend.dto.StudentHistoryEntry();
            historyEntry.setEntry(item.getEntry());
            historyEntry.setCreatedAt(item.getCreatedAt() == null ? null : item.getCreatedAt().toString());
            historyEntries.add(historyEntry);
        }
        response.setHistory(entries);
        response.setHistoryEntries(historyEntries);
        return response;
    }

    private static LocalDate parseDate(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return LocalDate.parse(value);
    }

    private static String trim(String value) {
        return value == null ? null : value.trim();
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private static String defaultIfBlank(String value, String fallback) {
        if (value == null || value.isBlank()) {
            return fallback;
        }
        return value.trim();
    }

    private static <T extends Enum<T>> T parseEnum(Class<T> type, String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        for (T constant : type.getEnumConstants()) {
            if (constant.name().equalsIgnoreCase(value.trim())) {
                return constant;
            }
        }
        return null;
    }
}
