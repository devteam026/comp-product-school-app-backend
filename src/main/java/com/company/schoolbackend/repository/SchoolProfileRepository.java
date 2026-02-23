package com.company.schoolbackend.repository;

import com.company.schoolbackend.entity.SchoolProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchoolProfileRepository extends JpaRepository<SchoolProfile, Long> {}
