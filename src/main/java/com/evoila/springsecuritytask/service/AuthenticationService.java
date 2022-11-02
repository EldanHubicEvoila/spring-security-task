package com.evoila.springsecuritytask.service;

import com.evoila.springsecuritytask.model.AuthRequest;
import com.evoila.springsecuritytask.model.AuthResponse;
import com.evoila.springsecuritytask.model.AuthUser;
import com.evoila.springsecuritytask.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@Service
public class AuthenticationService {

    @Autowired
    AuthenticationManager authManager;

    @Autowired
    JwtTokenUtil jwtUtil;

    public AuthResponse login(@RequestBody @Valid AuthRequest request) {
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(), request.getPassword())
            );

            AuthUser authUser = (AuthUser) authentication.getPrincipal();
            String accessToken = jwtUtil.generateAccessToken(authUser);

        return new AuthResponse(authUser.getUser().getUsername(),
                authUser.getUser().getEmail(),
                accessToken);
    }
}
