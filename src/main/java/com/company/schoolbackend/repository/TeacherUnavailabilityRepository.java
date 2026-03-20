package com.company.schoolbackend.repository;

import com.company.schoolbackend.entity.TeacherUnavailability;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherUnavailabilityRepository extends JpaRepository<TeacherUnavailability, Long> {
    List<TeacherUnavailability> findByTeacherId(Long teacherId);
    boolean existsByTeacherIdAndDayOfWeekAndPeriodNo(Long teacherId, String dayOfWeek, Integer periodNo);
}
