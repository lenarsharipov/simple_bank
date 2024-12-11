package com.lenarsharipov.simplebank.service;

import com.lenarsharipov.simplebank.exception.ResourceNotFoundException;
import com.lenarsharipov.simplebank.model.User;
import com.lenarsharipov.simplebank.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;
    private static final long USER_ID = 1L;
    private static final String USER_NAME = "test";

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setId(USER_ID);
        user.setUsername(USER_NAME);
    }

    @Test
    @DisplayName("Get user by username when user exists")
    public void getByUsername_UserExists_ReturnsUser() {
        when(userRepository.findByUsername(USER_NAME)).thenReturn(Optional.of(user));

        User foundUser = userService.getByUsername(USER_NAME);

        assertNotNull(foundUser);
        assertEquals(USER_NAME, foundUser.getUsername());
        verify(userRepository, times(1)).findByUsername(USER_NAME);
    }

    @Test
    @DisplayName("Get user by username when user does not exist")
    public void getByUsername_UserDoesNotExist_ThrowsException() {
        when(userRepository.findByUsername(USER_NAME)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getByUsername(USER_NAME));

        verify(userRepository, times(1)).findByUsername(USER_NAME);
    }

    @Test
    @DisplayName("Get user by ID when user exists")
    public void getById_UserExists_ReturnsUser() {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));

        User foundUser = userService.getById(USER_ID);

        assertNotNull(foundUser);
        assertEquals(USER_ID, foundUser.getId());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Get user by ID when user does not exist")
    public void getById_UserDoesNotExist_ThrowsException() {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getById(USER_ID));

        verify(userRepository, times(1)).findById(USER_ID);
    }
}