package com.evoila.springsecuritytask.service.impl;


import com.evoila.springsecuritytask.model.AuthenticationUser;
import com.evoila.springsecuritytask.repository.UserRepository;
import com.evoila.springsecuritytask.service.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class JpaUserService implements UserService {

    private final UserRepository userRepository;


    public UserDetails loadUserByUsername(String username) {
        return new AuthenticationUser(userRepository.findByUsername(username)
                .orElseThrow(
                        () -> new UsernameNotFoundException("User " + username + " not found")));
    }
}
