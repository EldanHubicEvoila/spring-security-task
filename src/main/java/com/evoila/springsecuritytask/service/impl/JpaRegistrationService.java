package com.evoila.springsecuritytask.service.impl;


import com.evoila.springsecuritytask.exception.UserAlreadyExistsException;
import com.evoila.springsecuritytask.model.AuthUser;
import com.evoila.springsecuritytask.model.ERole;
import com.evoila.springsecuritytask.model.Role;
import com.evoila.springsecuritytask.model.User;
import com.evoila.springsecuritytask.payload.request.RegistrationRequest;
import com.evoila.springsecuritytask.payload.response.RegistrationResponse;
import com.evoila.springsecuritytask.repository.UserRepository;
import com.evoila.springsecuritytask.service.RegistrationService;

import com.evoila.springsecuritytask.service.RoleService;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;


@Service
@RequiredArgsConstructor
public class JpaRegistrationService implements RegistrationService {

    private final UserRepository userRepository;

    private final RoleService roleService;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    public RegistrationResponse register(RegistrationRequest request) {
        if (userWithUsernameExists(request)) {
            throw new UserAlreadyExistsException("User with username " + request.getUsername() + " already exists");
        }

        if (userWithEmailExists(request)) {
            throw new UserAlreadyExistsException("User with email " + request.getEmail() + " already exists");
        }

        Set<Role> roles = new HashSet<>();
        AuthUser authUser = createAuthUser(request);

        if(isRoleUser(request)) {
            Role role = roleService.findRoleByName(ERole.USER);
            roles.add(role);
        } else if(isRoleAdmin(request)) {
            Role role = roleService.findRoleByName(ERole.ADMIN);
            roles.add(role);
        } else {
            throw new IllegalArgumentException("Invalid role: " + request.getRole());
        }

        authUser.getUser().setRoles(roles);

        userRepository.save(authUser.getUser());

        return RegistrationResponse.builder()
                .message("User registered successfully")
                .username(request.getUsername())
                .email(request.getEmail())
                .build();
    }

    private boolean userWithUsernameExists(RegistrationRequest registrationRequest) {
        return userRepository.existsByUsername(registrationRequest.getUsername());
    }

    private boolean userWithEmailExists(RegistrationRequest registrationRequest) {
        return userRepository.existsByEmail(registrationRequest.getEmail());
    }

    private boolean isRoleUser(RegistrationRequest registrationRequest) {
        return registrationRequest.getRole().toUpperCase().equals(ERole.USER.name());
    }

    private boolean isRoleAdmin(RegistrationRequest registrationRequest) {
        return registrationRequest.getRole().toUpperCase().equals(ERole.ADMIN.name());
    }

    private AuthUser createAuthUser(RegistrationRequest registrationRequest) {
        return new AuthUser(new User(
                registrationRequest.getUsername(),
                registrationRequest.getEmail(),
                bCryptPasswordEncoder.encode(registrationRequest.getPassword())));
    }
}
