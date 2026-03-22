package com.company.schoolbackend.service;

import com.company.schoolbackend.dto.StudentResponse;
import com.company.schoolbackend.dto.StudentUpsertRequest;
import com.company.schoolbackend.entity.FeeType;
import com.company.schoolbackend.entity.Gender;
import com.company.schoolbackend.entity.Student;
import com.company.schoolbackend.entity.StudentHistory;
import com.company.schoolbackend.entity.StudentStatus;
import com.company.schoolbackend.repository.StudentHistoryRepository;
import com.company.schoolbackend.repository.StudentRepository;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final StudentHistoryRepository historyRepository;

    public StudentService(StudentRepository studentRepository, StudentHistoryRepository historyRepository) {
        this.studentRepository = studentRepository;
        this.historyRepository = historyRepository;
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

    public StudentResponse upsert(StudentUpsertRequest request) {
        if (request == null || request.getName() == null || request.getName().isBlank()) {
            throw new IllegalArgumentException("Student name is required");
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
        String section = request.getSection() == null || request.getSection().isBlank() ? "-" : request.getSection().trim();
        student.setGrade(grade);
        student.setSection(section);
        student.setClassCode(grade + section);
        Gender genderValue = parseEnum(Gender.class, request.getGender());
        student.setGender(genderValue == null ? Gender.Male : genderValue);
        student.setDateOfBirth(parseDate(request.getDateOfBirth()));
        student.setAdmissionNumber(trim(request.getAdmissionNumber()));
        student.setRollNumber(trim(request.getRollNumber()));
        student.setAddress(trim(request.getAddress()));
        student.setParentName(trim(request.getParentName()));
        student.setParentRelation(trim(request.getParentRelation()));
        student.setParentPhone(trim(request.getParentPhone()));
        student.setParentEmail(trim(request.getParentEmail()));
        student.setParentOccupation(trim(request.getParentOccupation()));
        StudentStatus statusValue = parseEnum(StudentStatus.class, request.getStatus());
        FeeType feeTypeValue = parseEnum(FeeType.class, request.getFeeType());
        student.setStatus(statusValue == null ? StudentStatus.Active : statusValue);
        student.setFeeType(feeTypeValue == null ? FeeType.Paid : feeTypeValue);
        if (request.getProfilePhotoKey() != null && !request.getProfilePhotoKey().isBlank()) {
            student.setProfilePhotoKey(request.getProfilePhotoKey().trim());
        }
        student.setUpdatedAt(OffsetDateTime.now());

        Student saved = studentRepository.save(student);

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
        response.setRollNumber(student.getRollNumber());
        response.setAddress(student.getAddress());
        response.setParentName(student.getParentName());
        response.setParentRelation(student.getParentRelation());
        response.setParentPhone(student.getParentPhone());
        response.setParentEmail(student.getParentEmail());
        response.setParentOccupation(student.getParentOccupation());
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
