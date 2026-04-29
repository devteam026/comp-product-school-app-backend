package com.company.schoolbackend.service;

import com.company.schoolbackend.entity.SystemRole;
import com.company.schoolbackend.repository.SystemRoleRepository;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class SystemRoleService {
    private final SystemRoleRepository repository;

    public SystemRoleService(SystemRoleRepository repository) {
        this.repository = repository;
    }

    public List<Map<String, Object>> list() {
        return repository.findAll().stream()
                .map(r -> Map.<String, Object>of("id", r.getId(), "name", r.getName()))
                .toList();
    }

    public Map<String, Object> add(String name) {
        if (name == null || name.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role name is required.");
        }
        String trimmed = name.trim().toLowerCase();
        if (repository.existsByNameIgnoreCase(trimmed)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Role already exists.");
        }
        SystemRole role = new SystemRole();
        role.setName(trimmed);
        SystemRole saved = repository.save(role);
        return Map.of("id", saved.getId(), "name", saved.getName());
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found.");
        }
        repository.deleteById(id);
    }
}
