package com.company.schoolbackend.repository;

import com.company.schoolbackend.entity.TeacherRatingClass;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherRatingClassRepository extends JpaRepository<TeacherRatingClass, Long> {
    List<TeacherRatingClass> findByClassCode(String classCode);
    List<TeacherRatingClass> findByTeacherRatingIdIn(List<Long> teacherRatingIds);
}
