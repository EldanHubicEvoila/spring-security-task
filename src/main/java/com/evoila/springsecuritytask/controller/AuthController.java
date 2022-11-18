package com.evoila.springsecuritytask.controller;


import com.evoila.springsecuritytask.model.Role;
import com.evoila.springsecuritytask.payload.request.AuthenticationRequest;
import com.evoila.springsecuritytask.payload.request.RegistrationRequest;
import com.evoila.springsecuritytask.payload.response.AuthenticationResponse;
import com.evoila.springsecuritytask.payload.response.RegistrationResponse;
import com.evoila.springsecuritytask.service.AuthenticationService;

import com.evoila.springsecuritytask.service.RegistrationService;
import com.evoila.springsecuritytask.service.impl.JpaUserService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationService authenticationService;

    private final RegistrationService registrationService;


    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody @Valid AuthenticationRequest request) {
        return new ResponseEntity<>(authenticationService.login(request), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<RegistrationResponse> register(@RequestBody @Valid RegistrationRequest request) {
        return new ResponseEntity<>(registrationService.register(request), HttpStatus.OK);
    }
}
