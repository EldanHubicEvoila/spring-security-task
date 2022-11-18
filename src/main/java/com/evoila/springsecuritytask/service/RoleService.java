package com.evoila.springsecuritytask.service;

import com.evoila.springsecuritytask.model.ERole;
import com.evoila.springsecuritytask.model.Role;

public interface RoleService {

    Role findRoleByName(ERole roleName);
}
