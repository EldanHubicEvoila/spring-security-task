package com.evoila.springsecuritytask.repository;


import com.evoila.springsecuritytask.container.AbstractContainer;
import com.evoila.springsecuritytask.model.User;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class UserRepositoryTest extends AbstractContainer {

    @Autowired
    private UserRepository userRepository;


    @Test
    @DisplayName("findUserByUsername()_userWithUsernameInDatabase_returnUser")
    void findUserByUsername_whenUserExistsInDatabase_returnUser() {
        User user = new User(
                "testUser",
                "testuser@email.com",
                "testuser123"
        );

        userRepository.save(user);

        Optional<User> expectedUser = userRepository.findByUsername("testUser");

        assertEquals(expectedUser.orElse(new User()).getUsername(), "testUser");
    }

    @Test
    @DisplayName("findUserByUsername()_noUserWithUsername_throwNoSuchElementException")
    void findUserByUsername_whenUserDoesNotExistsInDatabase_throwNoSuchElementException() {
        Optional<User> expectedUser = userRepository.findByUsername("testUser");

        assertThrows(NoSuchElementException.class, expectedUser::get);
    }


}