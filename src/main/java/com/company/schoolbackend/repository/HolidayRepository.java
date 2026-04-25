package com.company.schoolbackend.repository;

import com.company.schoolbackend.entity.Holiday;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HolidayRepository extends JpaRepository<Holiday, Long> {
    Optional<Holiday> findByHolidayDate(LocalDate date);
    List<Holiday> findAllByOrderByHolidayDateAsc();
}
