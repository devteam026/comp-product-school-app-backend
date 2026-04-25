package com.company.schoolbackend.service;

import com.company.schoolbackend.dto.AttendanceListResponse;
import com.company.schoolbackend.dto.AttendanceRecordDto;
import com.company.schoolbackend.dto.AttendanceSaveRequest;
import com.company.schoolbackend.dto.AttendanceSaveResponse;
import com.company.schoolbackend.entity.AttendanceRecord;
import com.company.schoolbackend.entity.AttendanceStatus;
import com.company.schoolbackend.entity.Holiday;
import com.company.schoolbackend.entity.StudentStatus;
import com.company.schoolbackend.repository.AttendanceRecordRepository;
import com.company.schoolbackend.repository.StudentRepository;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class AttendanceService {
    private final AttendanceRecordRepository attendanceRecordRepository;
    private final StudentRepository studentRepository;
    private final HolidayService holidayService;

    public AttendanceService(AttendanceRecordRepository attendanceRecordRepository, StudentRepository studentRepository, HolidayService holidayService) {
        this.attendanceRecordRepository = attendanceRecordRepository;
        this.studentRepository = studentRepository;
        this.holidayService = holidayService;
    }

    public AttendanceSaveResponse save(AttendanceSaveRequest request) {
        if (request == null || request.getDate() == null) {
            throw new IllegalArgumentException("Date is required");
        }
        LocalDate date = LocalDate.parse(request.getDate());
        List<AttendanceRecordDto> records = request.getRecords();
        if (records == null || records.isEmpty()) {
            return new AttendanceSaveResponse(true, date.toString(), 0);
        }

        List<String> studentIds = new ArrayList<>();
        for (AttendanceRecordDto record : records) {
            if (record.getStudentId() != null) {
                studentIds.add(record.getStudentId());
            }
        }

        List<String> activeStudentIds = studentRepository.findAll()
                .stream()
                .filter(student -> student.getStatus() == StudentStatus.Active)
                .map(student -> student.getId())
                .toList();
        studentIds.retainAll(activeStudentIds);

        List<AttendanceRecord> existing = attendanceRecordRepository.findByAttendanceDateAndStudentIdIn(date, studentIds);
        Map<String, AttendanceRecord> existingMap = new HashMap<>();
        for (AttendanceRecord record : existing) {
            existingMap.put(record.getStudentId(), record);
        }

        int saved = 0;
        for (AttendanceRecordDto dto : records) {
            if (dto.getStudentId() == null || dto.getStatus() == null) {
                continue;
            }
            if (!studentIds.contains(dto.getStudentId())) {
                continue;
            }
            AttendanceRecord record = existingMap.getOrDefault(dto.getStudentId(), new AttendanceRecord());
            boolean isNew = record.getId() == null;
            record.setStudentId(dto.getStudentId());
            record.setAttendanceDate(date);
            record.setStatus(parseStatus(dto.getStatus()));
            if (isNew) {
                record.setCreatedAt(OffsetDateTime.now());
            }
            record.setUpdatedAt(OffsetDateTime.now());
            attendanceRecordRepository.save(record);
            saved += 1;
        }

        return new AttendanceSaveResponse(true, date.toString(), saved);
    }

    public AttendanceListResponse list(String dateValue) {
        LocalDate date = dateValue == null || dateValue.isBlank() ? LocalDate.now() : LocalDate.parse(dateValue);
        List<AttendanceRecord> records = attendanceRecordRepository.findByAttendanceDate(date);
        AttendanceListResponse response = new AttendanceListResponse();
        response.setDate(date.toString());
        int present = 0;
        int absent = 0;
        List<AttendanceRecordDto> dtos = new ArrayList<>();
        for (AttendanceRecord record : records) {
            dtos.add(new AttendanceRecordDto(record.getStudentId(), record.getStatus().name()));
            if (record.getStatus() == AttendanceStatus.Present) {
                present += 1;
            } else {
                absent += 1;
            }
        }
        response.setRecords(dtos);
        response.setPresentCount(present);
        response.setAbsentCount(absent);
        Optional<Holiday> holiday = holidayService.findByDate(date);
        if (holiday.isPresent()) {
            response.setHoliday(true);
            response.setHolidayName(holiday.get().getName());
        }
        return response;
    }

    private static AttendanceStatus parseStatus(String value) {
        if (value == null || value.isBlank()) {
            return AttendanceStatus.Present;
        }
        for (AttendanceStatus status : AttendanceStatus.values()) {
            if (status.name().equalsIgnoreCase(value.trim())) {
                return status;
            }
        }
        return AttendanceStatus.Present;
    }
}
