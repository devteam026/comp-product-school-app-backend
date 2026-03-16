package com.company.schoolbackend.repository;

import com.company.schoolbackend.entity.TeacherClass;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherClassRepository extends JpaRepository<TeacherClass, Long> {
    List<TeacherClass> findByTeacherUserId(Long teacherUserId);
    boolean existsByTeacherUserIdAndClassCode(Long teacherUserId, String classCode);
    void deleteByTeacherUserId(Long teacherUserId);
}
