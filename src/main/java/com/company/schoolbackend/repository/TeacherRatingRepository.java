package com.company.schoolbackend.repository;

import com.company.schoolbackend.entity.TeacherRating;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherRatingRepository extends JpaRepository<TeacherRating, Long> {
    List<TeacherRating> findAllByOrderByRatingDesc();
}
