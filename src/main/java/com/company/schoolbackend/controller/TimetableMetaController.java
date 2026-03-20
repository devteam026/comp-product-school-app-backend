package com.company.schoolbackend.controller;

import com.company.schoolbackend.dto.PeriodDto;
import com.company.schoolbackend.dto.SubjectDto;
import com.company.schoolbackend.dto.TeacherDto;
import com.company.schoolbackend.entity.Subject;
import com.company.schoolbackend.entity.Employee;
import com.company.schoolbackend.entity.TeacherSubject;
import com.company.schoolbackend.entity.TimetablePeriod;
import com.company.schoolbackend.repository.SubjectRepository;
import com.company.schoolbackend.repository.EmployeeRepository;
import com.company.schoolbackend.repository.TeacherSubjectRepository;
import com.company.schoolbackend.repository.TimetablePeriodRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/timetable/meta")
public class TimetableMetaController {
    private final EmployeeRepository employeeRepository;
    private final TeacherSubjectRepository teacherSubjectRepository;
    private final SubjectRepository subjectRepository;
    private final TimetablePeriodRepository periodRepository;

    public TimetableMetaController(
            EmployeeRepository employeeRepository,
            TeacherSubjectRepository teacherSubjectRepository,
            SubjectRepository subjectRepository,
            TimetablePeriodRepository periodRepository
    ) {
        this.employeeRepository = employeeRepository;
        this.teacherSubjectRepository = teacherSubjectRepository;
        this.subjectRepository = subjectRepository;
        this.periodRepository = periodRepository;
    }

    @GetMapping("/teachers")
    public List<TeacherDto> teachers() {
        List<Employee> teachers = employeeRepository.findAll().stream()
                .filter(e -> e.getDepartment() != null && e.getDepartment().equalsIgnoreCase("Teaching"))
                .collect(Collectors.toList());
        Map<Long, List<Long>> teacherSubjects = teacherSubjectRepository.findAll().stream()
                .collect(Collectors.groupingBy(TeacherSubject::getTeacherId,
                        Collectors.mapping(TeacherSubject::getSubjectId, Collectors.toList())));
        return teachers.stream()
                .map(t -> new TeacherDto(t.getId(), (t.getFirstName() + " " + t.getLastName()).trim(),
                        teacherSubjects.getOrDefault(t.getId(), new ArrayList<>())))
                .collect(Collectors.toList());
    }

    @GetMapping("/subjects")
    public List<SubjectDto> subjects() {
        List<Subject> subjects = subjectRepository.findAll();
        return subjects.stream()
                .map(s -> new SubjectDto(s.getId(), s.getName(), s.getColor()))
                .collect(Collectors.toList());
    }

    @GetMapping("/periods")
    public List<PeriodDto> periods() {
        List<TimetablePeriod> periods = periodRepository.findAll();
        return periods.stream()
                .map(p -> new PeriodDto(p.getId(), p.getDayOfWeek(), p.getPeriodNo(),
                        p.getStartTime().toString(), p.getEndTime().toString()))
                .collect(Collectors.toList());
    }
}
