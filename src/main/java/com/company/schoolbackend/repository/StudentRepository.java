package com.company.schoolbackend.repository;

import com.company.schoolbackend.entity.Gender;
import com.company.schoolbackend.entity.Student;
import com.company.schoolbackend.entity.StudentStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StudentRepository extends JpaRepository<Student, String> {
    @Query("""
        select s from Student s
        where (:classCode is null or s.classCode = :classCode)
          and (:gender is null or s.gender = :gender)
          and (:status is null or s.status = :status)
          and (:q is null or lower(s.name) like :q or lower(s.admissionNumber) like :q or lower(s.rollNumber) like :q)
    """)
    List<Student> search(
            @Param("classCode") String classCode,
            @Param("gender") Gender gender,
            @Param("status") StudentStatus status,
            @Param("q") String q
    );
}
