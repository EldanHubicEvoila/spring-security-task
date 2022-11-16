package com.evoila.springsecuritytask.repository;


import com.evoila.springsecuritytask.container.AbstractRepositoryTest;
import com.evoila.springsecuritytask.model.User;

import com.evoila.springsecuritytask.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


class UserRepositoryIntegrationTest extends AbstractRepositoryTest {

    @Autowired
    UserRepository userRepository;


    @Test
    @DisplayName("findUserByUsername(String username)_userWithUsernameInDatabase_returnUser")
    void findUserByUsername_whenUserExistsInDatabase_shouldReturnUser() {
        userRepository.save(new User("testUser", "testuser@email.com", "testUser123"));

        Optional<User> expectedUser = userRepository.findByUsername("testUser");

        assertAll(
                () -> assertEquals(expectedUser.orElse(new User()).getUsername(), "testUser"),
                () -> assertEquals(expectedUser.orElse(new User()).getEmail(), "testuser@email.com"));
    }

    @Test
    @DisplayName("findUserByUsername(String username)_noUserWithUsername_throwNoSuchElementException")
    void findUserByUsername_whenUserDoesNotExistsInDatabase_shouldThrowNoSuchElementException() {
        Optional<User> expectedUser = userRepository.findByUsername("testUser");

        assertThrows(NoSuchElementException.class, expectedUser::get);
    }

    @Test
    @DisplayName("existsByUsername(String username)_whenUserExistsWithSameUsername_shouldReturnTrue")
    void existsByUsername_whenUserWithSameUsernameExists_shouldReturnTrue() {
        userRepository.save(new User("testUser", "testuser@email.com", "testUser123"));

        boolean testUserExists = userRepository.existsByUsername("testUser");

        assertTrue(testUserExists);
    }

    @Test
    @DisplayName("existsByUsername(String username)_whenUserDoesNotExistsWithSameUsername_shouldReturnFalse")
    void existsByUsername_whenUserWithSameUsernameDoesNotExists_shouldReturnFalse() {
        userRepository.save(new User("testUser", "testuser@email.com", "testUser123"));

        boolean testUserExists = userRepository.existsByUsername("user");

        assertFalse(testUserExists);
    }

    @Test
    @DisplayName("existsByEmail(String email)_whenUserExistsWithSameEmail_shouldReturnTrue")
    void existsByEmail_whenUserWithSameEmailExists_shouldReturnTrue() {
        userRepository.save(new User("testUser", "testuser@email.com", "testUser123"));

        boolean testUserExists = userRepository.existsByEmail("testuser@email.com");

        assertTrue(testUserExists);
    }

    @Test
    @DisplayName("existsByUsername(String username)_whenUserDoesNotExistsWithSameEmail_shouldReturnFalse")
    void existsByUsername_whenUserWithSameEmailDoesNotExists_shouldReturnFalse() {
        userRepository.save(new User("testUser", "testuser@email.com", "testUser123"));

        boolean testUserExists = userRepository.existsByEmail("user@email.com");

        assertFalse(testUserExists);
    }
}