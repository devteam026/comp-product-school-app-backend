package com.company.schoolbackend.repository;

import com.company.schoolbackend.entity.TimetablePeriod;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimetablePeriodRepository extends JpaRepository<TimetablePeriod, Long> {
    List<TimetablePeriod> findByDayOfWeekOrderByPeriodNoAsc(String dayOfWeek);
    List<TimetablePeriod> findByClassId(Long classId);
    List<TimetablePeriod> findByClassIdIsNull();
    List<TimetablePeriod> findByClassIdOrClassIdIsNull(Long classId);
    Optional<TimetablePeriod> findByClassIdAndDayOfWeekAndPeriodNo(Long classId, String dayOfWeek, Integer periodNo);
    Optional<TimetablePeriod> findByClassIdAndDayOfWeekAndPeriodNoAndIdNot(Long classId, String dayOfWeek, Integer periodNo, Long id);
}
