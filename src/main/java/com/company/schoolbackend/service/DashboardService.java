package com.company.schoolbackend.service;

import com.company.schoolbackend.dto.AttendanceSummary;
import com.company.schoolbackend.dto.DailyAttendancePoint;
import com.company.schoolbackend.dto.DashboardResponse;
import com.company.schoolbackend.dto.FeeStats;
import com.company.schoolbackend.entity.AttendanceRecord;
import com.company.schoolbackend.entity.AttendanceStatus;
import com.company.schoolbackend.entity.FeeType;
import com.company.schoolbackend.entity.Gender;
import com.company.schoolbackend.entity.Student;
import com.company.schoolbackend.repository.AttendanceRecordRepository;
import com.company.schoolbackend.repository.PaymentRepository;
import com.company.schoolbackend.repository.StudentRepository;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {
    private final StudentRepository studentRepository;
    private final AttendanceRecordRepository attendanceRecordRepository;
    private final PaymentRepository paymentRepository;

    public DashboardService(
            StudentRepository studentRepository,
            AttendanceRecordRepository attendanceRecordRepository,
            PaymentRepository paymentRepository
    ) {
        this.studentRepository = studentRepository;
        this.attendanceRecordRepository = attendanceRecordRepository;
        this.paymentRepository = paymentRepository;
    }

    public DashboardResponse getDashboard(String classCode) {
        List<Student> students = studentRepository.findAll();
        if (classCode != null && !classCode.isBlank() && !"all".equalsIgnoreCase(classCode)) {
            students = students.stream().filter(s -> classCode.equals(s.getClassCode())).collect(Collectors.toList());
        }

        int total = students.size();
        int male = (int) students.stream().filter(s -> s.getGender() == Gender.Male).count();
        int female = (int) students.stream().filter(s -> s.getGender() == Gender.Female).count();

        LocalDate today = LocalDate.now();
        List<AttendanceRecord> todayRecords = attendanceRecordRepository.findByAttendanceDate(today);
        int present = (int) todayRecords.stream().filter(r -> r.getStatus() == AttendanceStatus.Present).count();
        int absent = todayRecords.size() - present;

        String month = today.toString().substring(0, 7);
        Set<String> paidStudentIds = paymentRepository.findByMonthsContains(month)
                .stream().map(payment -> payment.getStudentId()).collect(Collectors.toSet());

        int freeCount = (int) students.stream().filter(s -> s.getFeeType() == FeeType.Free).count();
        int paidCount = (int) students.stream().filter(s -> s.getFeeType() == FeeType.Paid && paidStudentIds.contains(s.getId())).count();
        int unpaidCount = (int) students.stream().filter(s -> s.getFeeType() == FeeType.Paid && !paidStudentIds.contains(s.getId())).count();

        List<DailyAttendancePoint> daily = new ArrayList<>();
        for (int i = 4; i >= 0; i -= 1) {
            LocalDate date = today.minusDays(i);
            List<AttendanceRecord> records = attendanceRecordRepository.findByAttendanceDate(date);
            int p = (int) records.stream().filter(r -> r.getStatus() == AttendanceStatus.Present).count();
            int a = records.size() - p;
            daily.add(new DailyAttendancePoint(shortDay(date.getDayOfWeek()), p, a));
        }

        DashboardResponse response = new DashboardResponse();
        response.setTotalStudents(total);
        response.setMaleCount(male);
        response.setFemaleCount(female);
        response.setAttendanceToday(new AttendanceSummary(present, absent));
        response.setFeeStats(new FeeStats(paidCount, unpaidCount, freeCount));
        response.setDailyAttendance(daily);
        return response;
    }

    private static String shortDay(DayOfWeek day) {
        return day.name().substring(0, 1) + day.name().substring(1, 3).toLowerCase();
    }
}
