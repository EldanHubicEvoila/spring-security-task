package com.evoila.springsecuritytask.service.impl;


import com.evoila.springsecuritytask.model.AuthUser;
import com.evoila.springsecuritytask.model.User;
import com.evoila.springsecuritytask.repository.UserRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@ExtendWith(MockitoExtension.class)
class JpaUserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private JpaUserService jpaUserService;

    private final AuthUser testUser = new AuthUser(new User(
            "testUsername",
            "testpasword123",
            "testemail@email.com"));


    @Test
    void loadUserByUsername_whenGivenUsername_shouldReturnUser_ifUserExists() {
        Mockito.when(userRepository.findByUsername(testUser.getUsername()))
                .thenReturn(Optional.of(testUser.getUser()));

        UserDetails expectedUser = jpaUserService.loadUserByUsername("testUsername");

        assertEquals(expectedUser.getUsername(), testUser.getUsername());
        Mockito.verify(userRepository).findByUsername("testUsername");
    }

    @Test
    void loadUserByUsername_whenGivenUsername_shouldThrowUsernameNotFoundException_ifUserDoesNotExists() {
        Mockito.when(userRepository.findByUsername("testUsername"))
                .thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> jpaUserService.loadUserByUsername("testUsername"));
    }
}