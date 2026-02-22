package com.company.schoolbackend.controller;

import com.company.schoolbackend.entity.AppUser;
import com.company.schoolbackend.entity.TeacherClass;
import com.company.schoolbackend.entity.UserRole;
import com.company.schoolbackend.repository.AppUserRepository;
import com.company.schoolbackend.repository.StudentRepository;
import com.company.schoolbackend.repository.TeacherClassRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/classes")
public class ClassController {
    private final StudentRepository studentRepository;
    private final AppUserRepository appUserRepository;
    private final TeacherClassRepository teacherClassRepository;

    public ClassController(
            StudentRepository studentRepository,
            AppUserRepository appUserRepository,
            TeacherClassRepository teacherClassRepository
    ) {
        this.studentRepository = studentRepository;
        this.appUserRepository = appUserRepository;
        this.teacherClassRepository = teacherClassRepository;
    }

    @GetMapping
    public List<String> listAll() {
        return studentRepository.findAll().stream()
                .map(student -> student.getClassCode())
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    @GetMapping("/teacher/{username}")
    public ResponseEntity<?> listTeacherClasses(@PathVariable String username) {
        AppUser user = appUserRepository.findByUsernameAndRole(username.toLowerCase(), UserRole.teacher).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        List<TeacherClass> classes = teacherClassRepository.findByTeacherUserId(user.getId());
        List<String> codes = classes.stream().map(TeacherClass::getClassCode).distinct().collect(Collectors.toList());
        return ResponseEntity.ok(codes);
    }
}
