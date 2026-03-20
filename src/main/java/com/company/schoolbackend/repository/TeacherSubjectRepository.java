package com.company.schoolbackend.repository;

import com.company.schoolbackend.entity.TeacherSubject;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherSubjectRepository extends JpaRepository<TeacherSubject, Long> {
    List<TeacherSubject> findByTeacherId(Long teacherId);
}
