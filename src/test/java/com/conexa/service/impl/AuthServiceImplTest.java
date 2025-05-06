package com.conexa.service.impl;

import com.conexa.entity.User;
import com.conexa.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class AuthServiceImplTest {

    @InjectMocks
    private AuthServiceImpl authService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void login_shouldReturnTrue_whenUsernameExistsAndPasswordMatches() {
        String username = "testuser";
        String rawPassword = "password";
        String encodedPassword = "encodedPassword";
        User mockUser = new User();
        mockUser.setUsername(username);
        mockUser.setPassword(encodedPassword);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(true);

        boolean result = authService.login(username, rawPassword);

        assertTrue(result);
        verify(userRepository, times(1)).findByUsername(username);
        verify(passwordEncoder, times(1)).matches(rawPassword, encodedPassword);
    }

    @Test
    void login_shouldReturnFalse_whenUsernameDoesNotExist() {
        String username = "nonexistentuser";
        String password = "anypassword";

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        boolean result = authService.login(username, password);

        assertFalse(result);
        verify(userRepository, times(1)).findByUsername(username);
        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }

    @Test
    void login_shouldReturnFalse_whenPasswordDoesNotMatch() {
        String username = "testuser";
        String rawPassword = "wrongpassword";
        String encodedPassword = "encodedPassword";
        User mockUser = new User();
        mockUser.setUsername(username);
        mockUser.setPassword(encodedPassword);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(false);

        boolean result = authService.login(username, rawPassword);

        assertFalse(result);
        verify(userRepository, times(1)).findByUsername(username);
        verify(passwordEncoder, times(1)).matches(rawPassword, encodedPassword);
    }

    @Test
    void register_shouldSaveNewUser_whenUsernameDoesNotExist() {
        String username = "newuser";
        String password = "newpassword";
        String encodedPassword = "encodedNewPassword";

        when(userRepository.existsByUsername(username)).thenReturn(false);
        when(passwordEncoder.encode(password)).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenReturn(new User());

        authService.register(username, password);

        verify(userRepository, times(1)).existsByUsername(username);
        verify(passwordEncoder, times(1)).encode(password);
        verify(userRepository, times(1)).save(argThat(user ->
                user.getUsername().equals(username) && user.getPassword().equals(encodedPassword)
        ));
    }

    @Test
    void register_shouldThrowRuntimeException_whenUsernameAlreadyExists() {
        String username = "existinguser";
        String password = "anypassword";

        when(userRepository.existsByUsername(username)).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.register(username, password);
        });

        assertEquals("El nombre de usuario ya está en uso.", exception.getMessage());
        }
}