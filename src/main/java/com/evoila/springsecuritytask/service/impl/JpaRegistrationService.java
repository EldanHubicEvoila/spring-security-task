package com.evoila.springsecuritytask.service.impl;


import com.evoila.springsecuritytask.exception.UserAlreadyExistsException;
import com.evoila.springsecuritytask.model.AuthUser;
import com.evoila.springsecuritytask.model.User;
import com.evoila.springsecuritytask.payload.request.RegistrationRequest;
import com.evoila.springsecuritytask.payload.response.RegistrationResponse;
import com.evoila.springsecuritytask.repository.UserRepository;
import com.evoila.springsecuritytask.service.RegistrationService;

import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class JpaRegistrationService implements RegistrationService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    public RegistrationResponse register(RegistrationRequest request) {
        if (userWithUsernameExists(request)) {
            throw new UserAlreadyExistsException("User with username " + request.getUsername() + " already exists");
        }

        if (userWithEmailExists(request)) {
            throw new UserAlreadyExistsException("User with email " + request.getEmail() + " already exists");
        }

        AuthUser authUser = createAuthUser(request);

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

    private AuthUser createAuthUser(RegistrationRequest registrationRequest) {
        return new AuthUser(new User(
                registrationRequest.getUsername(),
                registrationRequest.getEmail(),
                bCryptPasswordEncoder.encode(registrationRequest.getPassword())));
    }
}
