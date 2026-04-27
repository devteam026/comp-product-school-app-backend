package com.company.schoolbackend.service;

import com.company.schoolbackend.dto.ClassDto;
import com.company.schoolbackend.dto.PeriodDto;
import com.company.schoolbackend.dto.SubjectDto;
import com.company.schoolbackend.dto.TeacherDto;
import com.company.schoolbackend.dto.TimetableAssignmentDto;
import com.company.schoolbackend.dto.TimetableBulkRequest;
import com.company.schoolbackend.dto.TimetableCopyRequest;
import com.company.schoolbackend.dto.TimetableCreateRequest;
import com.company.schoolbackend.dto.TimetablePeriodRequest;
import com.company.schoolbackend.dto.TimetableResponse;
import com.company.schoolbackend.dto.TimetableSubjectRequest;
import com.company.schoolbackend.entity.SchoolClass;
import com.company.schoolbackend.entity.Employee;
import com.company.schoolbackend.entity.Subject;
import com.company.schoolbackend.entity.TeacherSubject;
import com.company.schoolbackend.entity.TimetableAssignment;
import com.company.schoolbackend.entity.TimetablePeriod;
import com.company.schoolbackend.repository.SchoolClassRepository;
import com.company.schoolbackend.repository.EmployeeRepository;
import com.company.schoolbackend.repository.SubjectRepository;
import com.company.schoolbackend.repository.TeacherSubjectRepository;
import com.company.schoolbackend.repository.TeacherUnavailabilityRepository;
import com.company.schoolbackend.repository.TimetableAssignmentRepository;
import com.company.schoolbackend.repository.TimetablePeriodRepository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TimetableService {
    private final TimetableAssignmentRepository assignmentRepository;
    private final TimetablePeriodRepository periodRepository;
    private final EmployeeRepository employeeRepository;
    private final TeacherSubjectRepository teacherSubjectRepository;
    private final SubjectRepository subjectRepository;
    private final SchoolClassRepository classRepository;
    private final TeacherUnavailabilityRepository unavailabilityRepository;

    public TimetableService(
            TimetableAssignmentRepository assignmentRepository,
            TimetablePeriodRepository periodRepository,
            EmployeeRepository employeeRepository,
            TeacherSubjectRepository teacherSubjectRepository,
            SubjectRepository subjectRepository,
            SchoolClassRepository classRepository,
            TeacherUnavailabilityRepository unavailabilityRepository
    ) {
        this.assignmentRepository = assignmentRepository;
        this.periodRepository = periodRepository;
        this.employeeRepository = employeeRepository;
        this.teacherSubjectRepository = teacherSubjectRepository;
        this.subjectRepository = subjectRepository;
        this.classRepository = classRepository;
        this.unavailabilityRepository = unavailabilityRepository;
    }

    public TimetableResponse getTimetable(String weekKey, Long classId, Long teacherId, Long subjectId) {
        String key = weekKey == null || weekKey.isBlank() ? "default" : weekKey;
        List<TimetableAssignment> assignments;
        if (classId != null) {
            assignments = assignmentRepository.findByWeekKeyAndClassId(key, classId);
        } else if (teacherId != null) {
            assignments = assignmentRepository.findByWeekKeyAndTeacherId(key, teacherId);
        } else if (subjectId != null) {
            assignments = assignmentRepository.findByWeekKeyAndSubjectId(key, subjectId);
        } else {
            assignments = assignmentRepository.findByWeekKey(key);
        }
        if (classId != null && teacherId != null) {
            assignments = assignments.stream()
                    .filter(a -> a.getTeacherId().equals(teacherId))
                    .collect(Collectors.toList());
        }
        if (classId != null && subjectId != null) {
            assignments = assignments.stream()
                    .filter(a -> a.getSubjectId().equals(subjectId))
                    .collect(Collectors.toList());
        }

        List<TimetablePeriod> periods = periodRepository.findAll();
        List<Employee> teachers = employeeRepository.findAll().stream()
                .filter(e -> e.getDepartment() != null && e.getDepartment().equalsIgnoreCase("Teaching"))
                .collect(Collectors.toList());
        List<Subject> subjects;
        if (classId != null) {
            subjects = subjectRepository.findByClassIdOrClassIdIsNull(classId);
            if (subjects.isEmpty()) {
                subjects = subjectRepository.findAll();
            }
        } else {
            subjects = subjectRepository.findAll();
        }
        List<TimetablePeriod> periodsForClass;
        if (classId != null) {
            periodsForClass = periodRepository.findByClassIdOrClassIdIsNull(classId);
            if (!periodsForClass.isEmpty()) {
                periods = periodsForClass;
            }
        }
        List<SchoolClass> classes = classRepository.findByActiveTrueOrderByClassCodeAsc();

        Map<Long, List<Long>> teacherSubjects = teacherSubjectRepository.findAll().stream()
                .collect(Collectors.groupingBy(TeacherSubject::getTeacherId,
                        Collectors.mapping(TeacherSubject::getSubjectId, Collectors.toList())));

        TimetableResponse response = new TimetableResponse();
        response.setWeekKey(key);
        response.setPeriods(periods.stream()
                .map(p -> new PeriodDto(p.getId(), p.getDayOfWeek(), p.getPeriodNo(),
                        p.getStartTime().toString(), p.getEndTime().toString(),
                        p.getStartDate() != null ? p.getStartDate().toString() : null,
                        p.getEndDate() != null ? p.getEndDate().toString() : null))
                .collect(Collectors.toList()));
        response.setAssignments(assignments.stream()
                .map(a -> new TimetableAssignmentDto(a.getId(), a.getTeacherId(), a.getClassId(),
                        a.getSubjectId(), a.getPeriodId(), a.getWeekKey(), a.isLocked()))
                .collect(Collectors.toList()));
        response.setTeachers(teachers.stream()
                .map(t -> new TeacherDto(t.getId(), (t.getFirstName() + " " + t.getLastName()).trim(),
                        teacherSubjects.getOrDefault(t.getId(), new ArrayList<>())))
                .collect(Collectors.toList()));
        response.setSubjects(subjects.stream()
                .map(s -> new SubjectDto(s.getId(), s.getName(), s.getColor()))
                .collect(Collectors.toList()));
        response.setClasses(classes.stream()
                .map(c -> new ClassDto(c.getId(), c.getName(), c.getSection(), c.getClassCode()))
                .collect(Collectors.toList()));
        return response;
    }

    public List<SubjectDto> listSubjects(Long classId) {
        List<Subject> subjects = classId == null
                ? subjectRepository.findAll()
                : subjectRepository.findByClassId(classId);
        Map<Long, String> classCodeMap = classRepository.findAll().stream()
                .collect(Collectors.toMap(SchoolClass::getId, SchoolClass::getClassCode));
        return subjects.stream()
                .map(s -> new SubjectDto(s.getId(), s.getName(), s.getColor(),
                        s.getClassId() != null ? classCodeMap.get(s.getClassId()) : null))
                .collect(Collectors.toList());
    }

    public SubjectDto createSubject(TimetableSubjectRequest request) {
        if (request == null || request.getClassId() == null
                || request.getName() == null || request.getName().isBlank()) {
            throw new IllegalArgumentException("Missing fields");
        }
        Subject subject = new Subject();
        subject.setClassId(request.getClassId());
        subject.setName(request.getName().trim());
        subject.setColor(request.getColor());
        Subject saved = subjectRepository.save(subject);
        return new SubjectDto(saved.getId(), saved.getName(), saved.getColor());
    }

    public SubjectDto updateSubject(Long id, TimetableSubjectRequest request) {
        if (request == null || request.getClassId() == null
                || request.getName() == null || request.getName().isBlank()) {
            throw new IllegalArgumentException("Missing fields");
        }
        Subject subject = subjectRepository.findById(id).orElseThrow();
        subject.setClassId(request.getClassId());
        subject.setName(request.getName().trim());
        subject.setColor(request.getColor());
        Subject saved = subjectRepository.save(subject);
        return new SubjectDto(saved.getId(), saved.getName(), saved.getColor());
    }

    @Transactional
    public void deleteSubject(Long id) {
        assignmentRepository.deleteBySubjectId(id);
        subjectRepository.deleteById(id);
    }

    public List<PeriodDto> listPeriods(Long classId) {
        List<TimetablePeriod> periods = classId == null
                ? periodRepository.findAll()
                : periodRepository.findByClassId(classId);
        Map<Long, String> classCodeMap = classRepository.findAll().stream()
                .collect(Collectors.toMap(SchoolClass::getId, SchoolClass::getClassCode));
        return periods.stream()
                .map(p -> new PeriodDto(p.getId(), p.getDayOfWeek(), p.getPeriodNo(),
                        p.getStartTime().toString(), p.getEndTime().toString(),
                        p.getStartDate() != null ? p.getStartDate().toString() : null,
                        p.getEndDate() != null ? p.getEndDate().toString() : null,
                        p.getClassId(), classCodeMap.getOrDefault(p.getClassId(), "")))
                .collect(Collectors.toList());
    }

    public PeriodDto createPeriod(TimetablePeriodRequest request) {
        if (request == null || request.getClassId() == null
                || request.getDayOfWeek() == null || request.getPeriodNo() == null
                || request.getStartTime() == null || request.getEndTime() == null) {
            throw new IllegalArgumentException("Missing fields");
        }
        if (request.getPeriodNo() < 1) {
            throw new IllegalArgumentException("Period number must be 1 or greater");
        }
        LocalTime start = LocalTime.parse(request.getStartTime());
        LocalTime end = LocalTime.parse(request.getEndTime());
        if (!end.isAfter(start)) {
            throw new IllegalArgumentException("End time must be after start time");
        }
        if (request.getStartDate() != null && request.getEndDate() != null) {
            LocalDate sd = LocalDate.parse(request.getStartDate());
            LocalDate ed = LocalDate.parse(request.getEndDate());
            if (ed.isBefore(sd)) {
                throw new IllegalArgumentException("End date must be on or after start date");
            }
        }
        periodRepository.findByClassIdAndDayOfWeekAndPeriodNo(
                request.getClassId(), request.getDayOfWeek().trim().toUpperCase(), request.getPeriodNo())
                .ifPresent(existing -> {
                    throw new IllegalArgumentException("A period already exists for this day and period number");
                });
        TimetablePeriod period = new TimetablePeriod();
        period.setClassId(request.getClassId());
        period.setDayOfWeek(request.getDayOfWeek().trim().toUpperCase());
        period.setPeriodNo(request.getPeriodNo());
        period.setStartTime(LocalTime.parse(request.getStartTime()));
        period.setEndTime(LocalTime.parse(request.getEndTime()));
        period.setStartDate(request.getStartDate() != null ? LocalDate.parse(request.getStartDate()) : null);
        period.setEndDate(request.getEndDate() != null ? LocalDate.parse(request.getEndDate()) : null);
        TimetablePeriod saved = periodRepository.save(period);
        return new PeriodDto(saved.getId(), saved.getDayOfWeek(), saved.getPeriodNo(),
                saved.getStartTime().toString(), saved.getEndTime().toString(),
                saved.getStartDate() != null ? saved.getStartDate().toString() : null,
                saved.getEndDate() != null ? saved.getEndDate().toString() : null);
    }

    public PeriodDto updatePeriod(Long id, TimetablePeriodRequest request) {
        if (request == null || request.getClassId() == null
                || request.getDayOfWeek() == null || request.getPeriodNo() == null
                || request.getStartTime() == null || request.getEndTime() == null) {
            throw new IllegalArgumentException("Missing fields");
        }
        if (request.getPeriodNo() < 1) {
            throw new IllegalArgumentException("Period number must be 1 or greater");
        }
        LocalTime start = LocalTime.parse(request.getStartTime());
        LocalTime end = LocalTime.parse(request.getEndTime());
        if (!end.isAfter(start)) {
            throw new IllegalArgumentException("End time must be after start time");
        }
        if (request.getStartDate() != null && request.getEndDate() != null) {
            LocalDate sd = LocalDate.parse(request.getStartDate());
            LocalDate ed = LocalDate.parse(request.getEndDate());
            if (ed.isBefore(sd)) {
                throw new IllegalArgumentException("End date must be on or after start date");
            }
        }
        periodRepository.findByClassIdAndDayOfWeekAndPeriodNoAndIdNot(
                request.getClassId(), request.getDayOfWeek().trim().toUpperCase(), request.getPeriodNo(), id)
                .ifPresent(existing -> {
                    throw new IllegalArgumentException("A period already exists for this day and period number");
                });
        TimetablePeriod period = periodRepository.findById(id).orElseThrow();
        period.setClassId(request.getClassId());
        period.setDayOfWeek(request.getDayOfWeek().trim().toUpperCase());
        period.setPeriodNo(request.getPeriodNo());
        period.setStartTime(LocalTime.parse(request.getStartTime()));
        period.setEndTime(LocalTime.parse(request.getEndTime()));
        period.setStartDate(request.getStartDate() != null ? LocalDate.parse(request.getStartDate()) : null);
        period.setEndDate(request.getEndDate() != null ? LocalDate.parse(request.getEndDate()) : null);
        TimetablePeriod saved = periodRepository.save(period);
        return new PeriodDto(saved.getId(), saved.getDayOfWeek(), saved.getPeriodNo(),
                saved.getStartTime().toString(), saved.getEndTime().toString(),
                saved.getStartDate() != null ? saved.getStartDate().toString() : null,
                saved.getEndDate() != null ? saved.getEndDate().toString() : null);
    }

    @Transactional
    public void deletePeriod(Long id) {
        assignmentRepository.deleteByPeriodId(id);
        periodRepository.deleteById(id);
    }

    public TimetableAssignmentDto create(TimetableCreateRequest request) {
        TimetableAssignment assignment = toAssignment(request, null);
        return toDto(assignmentRepository.save(assignment));
    }

    public TimetableAssignmentDto update(Long id, TimetableCreateRequest request) {
        TimetableAssignment existing = assignmentRepository.findById(id).orElseThrow();
        if (existing.isLocked()) {
            throw new IllegalArgumentException("Assignment is locked");
        }
        TimetableAssignment updated = toAssignment(request, existing);
        updated.setId(id);
        return toDto(assignmentRepository.save(updated));
    }

    public void delete(Long id) {
        TimetableAssignment existing = assignmentRepository.findById(id).orElseThrow();
        if (existing.isLocked()) {
            throw new IllegalArgumentException("Assignment is locked");
        }
        assignmentRepository.deleteById(id);
    }

    public void bulk(TimetableBulkRequest request) {
        if (request == null || request.getAssignments() == null) {
            return;
        }
        String weekKey = request.getWeekKey() == null || request.getWeekKey().isBlank()
                ? "default" : request.getWeekKey();
        for (TimetableCreateRequest item : request.getAssignments()) {
            item.setWeekKey(weekKey);
            TimetableAssignment existing = assignmentRepository
                    .findByWeekKeyAndPeriodIdAndClassId(weekKey, item.getPeriodId(), item.getClassId())
                    .orElse(null);
            if (existing == null) {
                create(item);
            } else {
                update(existing.getId(), item);
            }
        }
    }

    public void copy(TimetableCopyRequest request) {
        String fromWeek = request.getFromWeek() == null || request.getFromWeek().isBlank()
                ? "default" : request.getFromWeek();
        String toWeek = request.getToWeek() == null || request.getToWeek().isBlank()
                ? "default" : request.getToWeek();
        List<TimetableAssignment> assignments = request.getClassId() == null
                ? assignmentRepository.findByWeekKey(fromWeek)
                : assignmentRepository.findByWeekKeyAndClassId(fromWeek, request.getClassId());
        for (TimetableAssignment assignment : assignments) {
            TimetableAssignment copy = new TimetableAssignment();
            copy.setTeacherId(assignment.getTeacherId());
            copy.setClassId(assignment.getClassId());
            copy.setSubjectId(assignment.getSubjectId());
            copy.setPeriodId(assignment.getPeriodId());
            copy.setWeekKey(toWeek);
            copy.setLocked(false);
            copy.setCreatedAt(OffsetDateTime.now());
            copy.setUpdatedAt(OffsetDateTime.now());
            assignmentRepository.save(copy);
        }
    }

    public void setLock(Long id, boolean locked) {
        TimetableAssignment assignment = assignmentRepository.findById(id).orElseThrow();
        assignment.setLocked(locked);
        assignment.setUpdatedAt(OffsetDateTime.now());
        assignmentRepository.save(assignment);
    }

    private TimetableAssignment toAssignment(TimetableCreateRequest request, TimetableAssignment existing) {
        if (request == null || request.getTeacherId() == null || request.getClassId() == null
                || request.getSubjectId() == null || request.getPeriodId() == null) {
            throw new IllegalArgumentException("Missing fields");
        }
        String weekKey = request.getWeekKey() == null || request.getWeekKey().isBlank()
                ? "default" : request.getWeekKey();

        TimetablePeriod period = periodRepository.findById(request.getPeriodId()).orElseThrow();

        if (unavailabilityRepository.existsByTeacherIdAndDayOfWeekAndPeriodNo(
                request.getTeacherId(), period.getDayOfWeek(), period.getPeriodNo())) {
            throw new IllegalArgumentException("Teacher unavailable for this period");
        }

        if (existing == null) {
            assignmentRepository.findByWeekKeyAndPeriodIdAndTeacherId(weekKey, request.getPeriodId(), request.getTeacherId())
                    .ifPresent(item -> { throw new IllegalArgumentException("Teacher conflict"); });
            assignmentRepository.findByWeekKeyAndPeriodIdAndClassId(weekKey, request.getPeriodId(), request.getClassId())
                    .ifPresent(item -> { throw new IllegalArgumentException("Class conflict"); });
        } else {
            assignmentRepository.findByWeekKeyAndPeriodIdAndTeacherIdAndIdNot(weekKey, request.getPeriodId(), request.getTeacherId(), existing.getId())
                    .ifPresent(item -> { throw new IllegalArgumentException("Teacher conflict"); });
            assignmentRepository.findByWeekKeyAndPeriodIdAndClassIdAndIdNot(weekKey, request.getPeriodId(), request.getClassId(), existing.getId())
                    .ifPresent(item -> { throw new IllegalArgumentException("Class conflict"); });
        }

        TimetableAssignment assignment = existing == null ? new TimetableAssignment() : existing;
        assignment.setTeacherId(request.getTeacherId());
        assignment.setClassId(request.getClassId());
        assignment.setSubjectId(request.getSubjectId());
        assignment.setPeriodId(request.getPeriodId());
        assignment.setWeekKey(weekKey);
        if (assignment.getCreatedAt() == null) {
            assignment.setCreatedAt(OffsetDateTime.now());
        }
        assignment.setUpdatedAt(OffsetDateTime.now());
        return assignment;
    }

    private TimetableAssignmentDto toDto(TimetableAssignment assignment) {
        return new TimetableAssignmentDto(assignment.getId(), assignment.getTeacherId(), assignment.getClassId(),
                assignment.getSubjectId(), assignment.getPeriodId(), assignment.getWeekKey(), assignment.isLocked());
    }
}
