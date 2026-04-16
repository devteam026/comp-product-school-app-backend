package com.company.schoolbackend.service;

import com.company.schoolbackend.dto.AttendanceSummary;
import com.company.schoolbackend.dto.ClassAttendancePoint;
import com.company.schoolbackend.dto.DailyAttendancePoint;
import com.company.schoolbackend.dto.DashboardResponse;
import com.company.schoolbackend.dto.FeeStats;
import com.company.schoolbackend.entity.AttendanceRecord;
import com.company.schoolbackend.entity.AttendanceStatus;
import com.company.schoolbackend.entity.Gender;
import com.company.schoolbackend.entity.FeeDue;
import com.company.schoolbackend.entity.FeeDueStatus;
import com.company.schoolbackend.entity.Student;
import com.company.schoolbackend.entity.StudentStatus;
import com.company.schoolbackend.repository.AttendanceRecordRepository;
import com.company.schoolbackend.repository.FeeDueRepository;
import com.company.schoolbackend.repository.LeaveRequestRepository;
import com.company.schoolbackend.repository.StudentRepository;
import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {
    private final StudentRepository studentRepository;
    private final AttendanceRecordRepository attendanceRecordRepository;
    private final FeeDueRepository feeDueRepository;
    private final LeaveRequestRepository leaveRequestRepository;

    public DashboardService(
            StudentRepository studentRepository,
            AttendanceRecordRepository attendanceRecordRepository,
            FeeDueRepository feeDueRepository,
            LeaveRequestRepository leaveRequestRepository
    ) {
        this.studentRepository = studentRepository;
        this.attendanceRecordRepository = attendanceRecordRepository;
        this.feeDueRepository = feeDueRepository;
        this.leaveRequestRepository = leaveRequestRepository;
    }

    public DashboardResponse getDashboard(String classCode) {
        List<Student> students = studentRepository.findAll()
                .stream()
                .filter(student -> student.getStatus() == StudentStatus.Active)
                .collect(java.util.stream.Collectors.toList());
        if (classCode != null && !classCode.isBlank() && !"all".equalsIgnoreCase(classCode)) {
            students = students.stream().filter(s -> classCode.equals(s.getClassCode())).collect(Collectors.toList());
        }

        int total = students.size();
        int male = (int) students.stream().filter(s -> s.getGender() == Gender.Male).count();
        int female = (int) students.stream().filter(s -> s.getGender() == Gender.Female).count();

        LocalDate today = LocalDate.now();
        List<String> studentIds = students.stream().map(Student::getId).collect(Collectors.toList());
        List<AttendanceRecord> todayRecords = studentIds.isEmpty()
                ? new ArrayList<>()
                : attendanceRecordRepository.findByAttendanceDateAndStudentIdIn(today, studentIds);
        int present = (int) todayRecords.stream().filter(r -> r.getStatus() == AttendanceStatus.Present).count();
        int absent = todayRecords.size() - present;
        int notRecorded = Math.max(0, studentIds.size() - todayRecords.size());

        String month = today.toString().substring(0, 7);
        List<FeeDue> monthDues = feeDueRepository.findByDueDateStartingWith(month);
        int paidCount = (int) monthDues.stream().filter(d -> d.getStatus() == FeeDueStatus.PAID).count();
        int unpaidCount = (int) monthDues.stream().filter(d -> d.getStatus() == FeeDueStatus.UNPAID).count();
        int partialCount = (int) monthDues.stream().filter(d -> d.getStatus() == FeeDueStatus.PARTIAL).count();
        BigDecimal collectedAmount = monthDues.stream()
                .map(d -> d.getAmount().subtract(d.getRemainingAmount()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // New admissions this month
        YearMonth currentMonth = YearMonth.now();
        OffsetDateTime monthStart = currentMonth.atDay(1).atStartOfDay().atOffset(java.time.ZoneOffset.UTC);
        OffsetDateTime monthEnd = currentMonth.atEndOfMonth().atTime(23, 59, 59).atOffset(java.time.ZoneOffset.UTC);
        long newAdmissions = studentRepository.findAll().stream()
                .filter(s -> s.getCreatedAt() != null
                        && !s.getCreatedAt().isBefore(monthStart)
                        && !s.getCreatedAt().isAfter(monthEnd))
                .count();

        // Pending leave requests
        long pendingLeaves = leaveRequestRepository.countByStatus("Pending");

        List<DailyAttendancePoint> daily = new ArrayList<>();
        for (int i = 29; i >= 0; i -= 1) {
            LocalDate date = today.minusDays(i);
            List<AttendanceRecord> records = studentIds.isEmpty()
                    ? new ArrayList<>()
                    : attendanceRecordRepository.findByAttendanceDateAndStudentIdIn(date, studentIds);
            int p = (int) records.stream().filter(r -> r.getStatus() == AttendanceStatus.Present).count();
            int a = records.size() - p;
            daily.add(new DailyAttendancePoint(shortDay(date.getDayOfWeek()), p, a));
        }

        Map<String, List<Student>> byClass = students.stream()
                .collect(Collectors.groupingBy(Student::getClassCode));
        List<ClassAttendancePoint> classAttendance = new ArrayList<>();
        List<ClassAttendancePoint> classStudentCounts = new ArrayList<>();
        for (var entry : byClass.entrySet()) {
            List<String> ids = entry.getValue().stream().map(Student::getId).collect(Collectors.toList());
            List<AttendanceRecord> records = ids.isEmpty()
                    ? new ArrayList<>()
                    : attendanceRecordRepository.findByAttendanceDateAndStudentIdIn(today, ids);
            int p = (int) records.stream().filter(r -> r.getStatus() == AttendanceStatus.Present).count();
            int a = records.size() - p;
            classAttendance.add(new ClassAttendancePoint(entry.getKey(), p, a));
            classStudentCounts.add(new ClassAttendancePoint(entry.getKey(), entry.getValue().size(), 0));
        }
        classAttendance.sort((a, b) -> a.getClassCode().compareToIgnoreCase(b.getClassCode()));
        classStudentCounts.sort((a, b) -> a.getClassCode().compareToIgnoreCase(b.getClassCode()));

        FeeStats feeStats = new FeeStats(paidCount, unpaidCount, partialCount);
        feeStats.setCollectedAmount(collectedAmount);

        DashboardResponse response = new DashboardResponse();
        response.setTotalStudents(total);
        response.setMaleCount(male);
        response.setFemaleCount(female);
        response.setAttendanceToday(new AttendanceSummary(present, absent, notRecorded));
        response.setFeeStats(feeStats);
        response.setDailyAttendance(daily);
        response.setClassAttendance(classAttendance);
        response.setClassStudentCounts(classStudentCounts);
        response.setNewAdmissionsThisMonth((int) newAdmissions);
        response.setPendingLeaveCount((int) pendingLeaves);
        return response;
    }

    private static String shortDay(DayOfWeek day) {
        return day.name().substring(0, 1) + day.name().substring(1, 3).toLowerCase();
    }
}
