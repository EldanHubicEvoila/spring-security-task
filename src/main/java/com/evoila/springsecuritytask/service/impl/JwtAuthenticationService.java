package com.evoila.springsecuritytask.service.impl;


import com.evoila.springsecuritytask.model.AuthUser;
import com.evoila.springsecuritytask.payload.request.AuthenticationRequest;
import com.evoila.springsecuritytask.payload.response.AuthenticationResponse;
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

    private final JwtTokenUtil jwtUtil;

    private final AuthenticationManager authManager;


    public AuthenticationResponse login(AuthenticationRequest request) {
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(), request.getPassword())
        );

        AuthUser authUser = (AuthUser) authentication.getPrincipal();
        String accessToken = jwtUtil.generateAccessToken(authUser);

        return AuthenticationResponse
                .builder()
                .username(authUser.getUsername())
                .email(authUser.getUser().getEmail())
                .accessToken(accessToken)
                .build();
    }
}
