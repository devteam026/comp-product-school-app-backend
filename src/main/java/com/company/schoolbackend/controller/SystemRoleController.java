package com.company.schoolbackend.controller;

import com.company.schoolbackend.dto.UserProfile;
import com.company.schoolbackend.service.SystemRoleService;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/system-roles")
public class SystemRoleController {
    private final SystemRoleService service;

    public SystemRoleController(SystemRoleService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> list() {
        return ResponseEntity.ok(service.list());
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> add(@RequestBody Map<String, String> body,
                                                    HttpServletRequest request) {
        if (!isAdmin(request)) return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        return ResponseEntity.ok(service.add(body.get("name")));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, HttpServletRequest request) {
        if (!isAdmin(request)) return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        service.delete(id);
        return ResponseEntity.ok().build();
    }

    private boolean isAdmin(HttpServletRequest request) {
        Object profileObj = request.getAttribute("userProfile");
        if (profileObj instanceof UserProfile profile) {
            return "admin".equalsIgnoreCase(profile.getRole());
        }
        return false;
    }
}
