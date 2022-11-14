package com.evoila.springsecuritytask.repository;


import com.evoila.springsecuritytask.container.AbstractRepositoryTest;
import com.evoila.springsecuritytask.model.User;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


class UserRepositoryIntegrationTest extends AbstractRepositoryTest {

    @Autowired
    UserRepository userRepository;


    @Test
    @DisplayName("findUserByUsername(String username)_userWithUsernameInDatabase_returnUser")
    void findUserByUsername_whenUserExistsInDatabase_shouldReturnUser() {
        userRepository.save(new User("testUser", "testuser@email.com", "testuser123"));

        Optional<User> expectedUser = userRepository.findByUsername("testUser");

        assertEquals(expectedUser.orElse(new User()).getUsername(), "testUser");
    }

    @Test
    @DisplayName("findUserByUsername(String username)_noUserWithUsername_throwNoSuchElementException")
    void findUserByUsername_whenUserDoesNotExistsInDatabase_shouldThrowNoSuchElementException() {
        Optional<User> expectedUser = userRepository.findByUsername("testUser");

        assertThrows(NoSuchElementException.class, expectedUser::get);
    }
}