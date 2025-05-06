package com.conexa.controller;

import com.conexa.model.LoginResponse;
import com.conexa.model.UserRequest;
import com.conexa.security.JwtUtil;
import com.conexa.service.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private AuthService authService;

    @Mock
    private JwtUtil jwtUtil;

    @Test
    void login_shouldReturnOkWithToken_whenAuthenticationSuccessful() {

        UserRequest userRequest = new UserRequest("testuser", "password");
        when(authService.login(userRequest.getUsername(), userRequest.getPassword())).thenReturn(true);
        when(jwtUtil.generateToken(userRequest.getUsername())).thenReturn("mockedToken");

        ResponseEntity<?> responseEntity = authController.login(userRequest);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        LoginResponse loginResponse = (LoginResponse) responseEntity.getBody();
        assertEquals(userRequest.getUsername(), loginResponse.getUsername());
        assertEquals("mockedToken", loginResponse.getToken());
    }

    @Test
    void login_shouldReturnUnauthorized_whenAuthenticationFails() {
        UserRequest userRequest = new UserRequest("invaliduser", "wrongpassword");
        when(authService.login(userRequest.getUsername(), userRequest.getPassword())).thenReturn(false);

        ResponseEntity<?> responseEntity = authController.login(userRequest);

        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }

    @Test
    void register_shouldReturnCreated_whenRegistrationSuccessful() {
        UserRequest userRequest = new UserRequest("newuser", "newpassword");
        doNothing().when(authService).register(userRequest.getUsername(), userRequest.getPassword());

        ResponseEntity<?> responseEntity = authController.register(userRequest);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }

    @Test
    void register_shouldReturnBadRequestWithError_whenRegistrationFails() {
        UserRequest userRequest = new UserRequest("existinguser", "anypassword");
        String errorMessage = "El nombre de usuario ya está en uso.";
        doThrow(new RuntimeException(errorMessage)).when(authService).register(userRequest.getUsername(), userRequest.getPassword());

        ResponseEntity<?> responseEntity = authController.register(userRequest);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Error al registrar el usuario: " + errorMessage, responseEntity.getBody());
    }
}