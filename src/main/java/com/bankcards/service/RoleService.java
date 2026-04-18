package com.bankcards.service;

import com.bankcards.entity.Role;
import com.bankcards.repository.RoleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    // List all roles
    @Transactional(readOnly = true)
    public List<Role> listALl() {
        return roleRepository.findAll();
    }

    // Get role or fail fast with message
    @Transactional(readOnly = true)
    public Role getRequiredRole(String roleName) {
        return roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));
    }
}