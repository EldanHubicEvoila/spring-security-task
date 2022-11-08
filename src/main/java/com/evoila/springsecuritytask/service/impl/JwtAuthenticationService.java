package com.evoila.springsecuritytask.service.impl;


import com.evoila.springsecuritytask.model.AuthenticationRequest;
import com.evoila.springsecuritytask.model.AuthenticationResponse;
import com.evoila.springsecuritytask.model.AuthenticationUser;
import com.evoila.springsecuritytask.service.AuthenticationService;
import com.evoila.springsecuritytask.util.JwtTokenUtil;

import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class JwtAuthenticationService implements AuthenticationService {

    private final AuthenticationManager authManager;
    private final JwtTokenUtil jwtUtil;


    public AuthenticationResponse login(AuthenticationRequest request) {
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(), request.getPassword())
        );

        AuthenticationUser authenticationUser = (AuthenticationUser) authentication.getPrincipal();
        String accessToken = jwtUtil.generateAccessToken(authenticationUser);

        return AuthenticationResponse
                .builder()
                .username(authenticationUser.getUsername())
                .email(authenticationUser.getUser().getEmail())
                .accessToken(accessToken)
                .build();
    }
}
