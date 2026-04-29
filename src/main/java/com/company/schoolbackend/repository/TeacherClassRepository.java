package com.company.schoolbackend.repository;

import com.company.schoolbackend.entity.TeacherClass;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface TeacherClassRepository extends JpaRepository<TeacherClass, Long> {
    List<TeacherClass> findByTeacherUserId(Long teacherUserId);
    boolean existsByTeacherUserIdAndClassCode(Long teacherUserId, String classCode);
    @Transactional
    void deleteByTeacherUserId(Long teacherUserId);
}
