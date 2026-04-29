package com.company.schoolbackend.controller;

import com.company.schoolbackend.dto.EmployeeResponse;
import com.company.schoolbackend.dto.EmployeeUpsertRequest;
import com.company.schoolbackend.dto.EmployeeRoleRequest;
import com.company.schoolbackend.dto.EmployeeClassAssignRequest;
import com.company.schoolbackend.dto.UserProfile;
import com.company.schoolbackend.service.EmployeeService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public ResponseEntity<List<EmployeeResponse>> list(HttpServletRequest request) {
        if (!isAdmin(request)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return ResponseEntity.ok(employeeService.list());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponse> get(@PathVariable Long id, HttpServletRequest request) {
        if (!isAdmin(request)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return ResponseEntity.ok(employeeService.get(id));
    }

    @PostMapping
    public ResponseEntity<EmployeeResponse> create(@RequestBody EmployeeUpsertRequest requestBody,
                                                   HttpServletRequest request) {
        if (!isAdmin(request)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return ResponseEntity.ok(employeeService.upsert(requestBody));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponse> update(@PathVariable Long id,
                                                   @RequestBody EmployeeUpsertRequest requestBody,
                                                   HttpServletRequest request) {
        if (!isAdmin(request)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        requestBody.setId(id);
        return ResponseEntity.ok(employeeService.upsert(requestBody));
    }

    @GetMapping("/{id}/classes")
    public ResponseEntity<List<String>> getClasses(@PathVariable Long id, HttpServletRequest request) {
        if (!isAdmin(request)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return ResponseEntity.ok(employeeService.getTeacherClasses(id));
    }

    @PostMapping("/{id}/classes")
    public ResponseEntity<?> assignClasses(@PathVariable Long id,
                                           @RequestBody EmployeeClassAssignRequest requestBody,
                                           HttpServletRequest request) {
        if (!isAdmin(request)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        employeeService.assignTeacherClasses(id, requestBody.getClassCodes());
        return ResponseEntity.ok(java.util.Map.of("ok", true));
    }

    @PostMapping("/{id}/role")
    public ResponseEntity<?> assignRole(@PathVariable Long id,
                                        @RequestBody EmployeeRoleRequest requestBody,
                                        HttpServletRequest request) {
        if (!isAdmin(request)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        employeeService.assignRole(id, requestBody.getRole(), requestBody.getPassword(), requestBody.getUpdatePassword(), requestBody.getActive());
        return ResponseEntity.ok(java.util.Map.of("ok", true));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, HttpServletRequest request) {
        if (!isAdmin(request)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        employeeService.delete(id);
        return ResponseEntity.ok(java.util.Map.of("ok", true));
    }

    @DeleteMapping("/{id}/role")
    public ResponseEntity<?> deleteRole(@PathVariable Long id, HttpServletRequest request) {
        if (!isAdmin(request)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        employeeService.deleteRole(id);
        return ResponseEntity.ok(java.util.Map.of("ok", true));
    }

    private boolean isAdmin(HttpServletRequest request) {
        Object profileObj = request.getAttribute("userProfile");
        if (profileObj instanceof UserProfile profile) {
            return "admin".equalsIgnoreCase(profile.getRole());
        }
        return false;
    }
}
