package com.company.schoolbackend.repository;

import com.company.schoolbackend.entity.Subject;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
    List<Subject> findByClassId(Long classId);
    List<Subject> findByClassIdIsNull();
    List<Subject> findByClassIdOrClassIdIsNull(Long classId);
}
