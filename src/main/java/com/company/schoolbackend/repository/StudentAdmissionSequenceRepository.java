package com.company.schoolbackend.repository;

import com.company.schoolbackend.entity.StudentAdmissionSequence;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentAdmissionSequenceRepository extends JpaRepository<StudentAdmissionSequence, Long> {
}
