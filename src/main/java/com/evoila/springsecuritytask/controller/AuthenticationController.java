package com.evoila.springsecuritytask.controller;


import com.evoila.springsecuritytask.model.AuthenticationRequest;
import com.evoila.springsecuritytask.model.AuthenticationResponse;
import com.evoila.springsecuritytask.service.AuthenticationService;

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
public class AuthenticationController {

    private final AuthenticationService authenticationService;


    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody @Valid AuthenticationRequest request) {
        return new ResponseEntity<>(authenticationService.login(request), HttpStatus.OK);
    }
}
