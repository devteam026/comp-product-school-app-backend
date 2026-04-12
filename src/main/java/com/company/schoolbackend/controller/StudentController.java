package com.company.schoolbackend.controller;

import com.company.schoolbackend.dto.GuardianInfo;
import com.company.schoolbackend.dto.StudentResponse;
import com.company.schoolbackend.dto.StudentUpsertRequest;
import com.company.schoolbackend.service.StudentService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/students")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public List<StudentResponse> list(
            @RequestParam(value = "classCode", required = false) String classCode,
            @RequestParam(value = "gender", required = false) String gender,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "q", required = false) String q
    ) {
        return studentService.list(classCode, gender, status, q);
    }

    @GetMapping("/guardians")
    public List<GuardianInfo> guardians(@RequestParam(value = "q", required = false) String q) {
        return studentService.searchGuardians(q);
    }

    @GetMapping("/{id}")
    public StudentResponse get(@PathVariable String id) {
        return studentService.get(id);
    }

    @PostMapping
    public StudentResponse create(@RequestBody StudentUpsertRequest request) {
        return studentService.upsert(request);
    }

    @PutMapping("/{id}")
    public StudentResponse update(@PathVariable String id, @RequestBody StudentUpsertRequest request) {
        request.setId(id);
        return studentService.upsert(request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        studentService.delete(id);
        return ResponseEntity.ok(java.util.Map.of("ok", true));
    }
}
