package com.company.schoolbackend.repository;

import com.company.schoolbackend.entity.AttendanceRecord;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendanceRecordRepository extends JpaRepository<AttendanceRecord, Long> {
    List<AttendanceRecord> findByAttendanceDate(LocalDate date);
    List<AttendanceRecord> findByAttendanceDateAndStudentIdIn(LocalDate date, List<String> studentIds);
}
