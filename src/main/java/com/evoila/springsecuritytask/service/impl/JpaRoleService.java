package com.evoila.springsecuritytask.service.impl;

import com.evoila.springsecuritytask.model.ERole;
import com.evoila.springsecuritytask.model.Role;
import com.evoila.springsecuritytask.repository.RoleRepository;
import com.evoila.springsecuritytask.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JpaRoleService implements RoleService {

    private final RoleRepository roleRepository;


    @Override
    public Role findRoleByName(ERole roleName) {
        return roleRepository.findRoleByName(roleName.name()).orElse(new Role());
    }
}
