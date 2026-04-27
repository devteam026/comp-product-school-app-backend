package com.company.schoolbackend.controller;

import com.company.schoolbackend.dto.ClassDto;
import com.company.schoolbackend.dto.ClassUpsertRequest;
import com.company.schoolbackend.dto.UserProfile;
import com.company.schoolbackend.entity.AppUser;
import com.company.schoolbackend.entity.SchoolClass;
import com.company.schoolbackend.entity.TeacherClass;
import com.company.schoolbackend.entity.UserRole;
import com.company.schoolbackend.repository.AppUserRepository;
import com.company.schoolbackend.repository.SchoolClassRepository;
import com.company.schoolbackend.repository.TeacherClassRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/classes")
public class ClassController {
    private final AppUserRepository appUserRepository;
    private final TeacherClassRepository teacherClassRepository;
    private final SchoolClassRepository schoolClassRepository;

    public ClassController(
            AppUserRepository appUserRepository,
            TeacherClassRepository teacherClassRepository,
            SchoolClassRepository schoolClassRepository
    ) {
        this.appUserRepository = appUserRepository;
        this.teacherClassRepository = teacherClassRepository;
        this.schoolClassRepository = schoolClassRepository;
    }

    @GetMapping
    public List<String> listAll() {
        return schoolClassRepository.findByActiveTrueOrderByClassCodeAsc().stream()
                .map(SchoolClass::getClassCode)
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

    @GetMapping("/manage")
    public ResponseEntity<List<ClassDto>> listManage(HttpServletRequest request) {
        if (!isAdmin(request)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        List<ClassDto> list = schoolClassRepository.findAll().stream()
                .map(c -> new ClassDto(c.getId(), c.getName(), c.getSection(), c.getClassCode(), c.getMaxStrength()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @PostMapping("/manage")
    public ResponseEntity<ClassDto> create(@RequestBody ClassUpsertRequest requestBody, HttpServletRequest request) {
        if (!isAdmin(request)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        SchoolClass saved = upsert(null, requestBody);
        return ResponseEntity.ok(new ClassDto(saved.getId(), saved.getName(), saved.getSection(), saved.getClassCode(), saved.getMaxStrength()));
    }

    @PutMapping("/manage/{id}")
    public ResponseEntity<ClassDto> update(@PathVariable Long id, @RequestBody ClassUpsertRequest requestBody, HttpServletRequest request) {
        if (!isAdmin(request)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        SchoolClass saved = upsert(id, requestBody);
        return ResponseEntity.ok(new ClassDto(saved.getId(), saved.getName(), saved.getSection(), saved.getClassCode(), saved.getMaxStrength()));
    }

    @DeleteMapping("/manage/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, HttpServletRequest request) {
        if (!isAdmin(request)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        schoolClassRepository.deleteById(id);
        return ResponseEntity.ok(java.util.Map.of("ok", true));
    }

    private SchoolClass upsert(Long id, ClassUpsertRequest request) {
        if (request == null || request.getClassCode() == null || request.getClassCode().isBlank()
                || request.getName() == null || request.getName().isBlank()) {
            throw new IllegalArgumentException("Missing fields");
        }
        SchoolClass entity = id == null ? new SchoolClass() : schoolClassRepository.findById(id).orElse(new SchoolClass());
        entity.setClassCode(request.getClassCode().trim());
        entity.setName(request.getName().trim());
        entity.setSection(request.getSection());
        entity.setGrade(request.getGrade());
        entity.setMaxStrength(request.getMaxStrength());
        if (request.getActive() != null) {
            entity.setActive(request.getActive());
        }
        return schoolClassRepository.save(entity);
    }

    private boolean isAdmin(HttpServletRequest request) {
        Object profileObj = request.getAttribute("userProfile");
        if (profileObj instanceof UserProfile profile) {
            return "admin".equalsIgnoreCase(profile.getRole());
        }
        return false;
    }
}
