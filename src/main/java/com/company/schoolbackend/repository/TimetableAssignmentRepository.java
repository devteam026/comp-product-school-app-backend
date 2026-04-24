package com.company.schoolbackend.repository;

import com.company.schoolbackend.entity.TimetableAssignment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimetableAssignmentRepository extends JpaRepository<TimetableAssignment, Long> {
    List<TimetableAssignment> findByWeekKey(String weekKey);
    List<TimetableAssignment> findByWeekKeyAndClassId(String weekKey, Long classId);
    List<TimetableAssignment> findByWeekKeyAndTeacherId(String weekKey, Long teacherId);
    List<TimetableAssignment> findByWeekKeyAndSubjectId(String weekKey, Long subjectId);
    Optional<TimetableAssignment> findByWeekKeyAndPeriodIdAndTeacherId(String weekKey, Long periodId, Long teacherId);
    Optional<TimetableAssignment> findByWeekKeyAndPeriodIdAndClassId(String weekKey, Long periodId, Long classId);
    Optional<TimetableAssignment> findByWeekKeyAndPeriodIdAndTeacherIdAndIdNot(String weekKey, Long periodId, Long teacherId, Long id);
    Optional<TimetableAssignment> findByWeekKeyAndPeriodIdAndClassIdAndIdNot(String weekKey, Long periodId, Long classId, Long id);
    List<TimetableAssignment> findByWeekKeyAndClassIdAndPeriodIdIn(String weekKey, Long classId, List<Long> periodIds);
    void deleteByPeriodId(Long periodId);
    void deleteBySubjectId(Long subjectId);
}
