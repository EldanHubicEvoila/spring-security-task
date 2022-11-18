package com.evoila.springsecuritytask.service.impl;


import com.evoila.springsecuritytask.model.AuthUser;
import com.evoila.springsecuritytask.repository.UserRepository;
import com.evoila.springsecuritytask.service.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;


@Service
@RequiredArgsConstructor
public class JpaUserService implements UserService {

    private final UserRepository userRepository;


    public UserDetails loadUserByUsername(String username) {
        return new AuthUser(userRepository.findByUsername(username)
                .orElseThrow(
                        () -> new UsernameNotFoundException("User " + username + " not found")));
    }

//    private Set<SimpleGrantedAuthority> getAuthority(AuthUser authUser) {
//        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
//
//        authUser.getUser().getRoles().forEach(role -> {
//            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
//        });
//
//        return authorities;
//    }
}
