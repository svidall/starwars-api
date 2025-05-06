package com.conexa.controller;

import com.conexa.model.LoginResponse;
import com.conexa.model.UserRequest;
import com.conexa.security.JwtUtil;
import com.conexa.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
@Tag(name = "Autenticación", description = "Operaciones relacionadas con la autenticación de usuarios")
@RequiredArgsConstructor
public class AuthController {

    private final JwtUtil jwtUtil;
    private final AuthService authService;

    @Operation(
            summary = "Iniciar sesión de usuario",
            description = "Autentica a un usuario existente y genera un token JWT.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Credenciales del usuario para iniciar sesión",
                    required = true,
                    content = @Content(schema = @Schema(implementation = UserRequest.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Inicio de sesión exitoso",
                            content = @Content(schema = @Schema(implementation = LoginResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Credenciales inválidas")
            }
    )
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Validated UserRequest user) {
        boolean isUserValid = authService.login(user.getUsername(), user.getPassword());
        if (isUserValid) {
            String token = jwtUtil.generateToken(user.getUsername());
            return ResponseEntity.ok(new LoginResponse(user.getUsername(), token));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @Operation(
            summary = "Registrar un nuevo usuario",
            description = "Crea una nueva cuenta de usuario.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Información del usuario a registrar",
                    required = true,
                    content = @Content(schema = @Schema(implementation = UserRequest.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Usuario registrado exitosamente"),
                    @ApiResponse(responseCode = "400", description = "Error al registrar el usuario")
            }
    )
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Validated UserRequest user) {
        try {
            authService.register(user.getUsername(), user.getPassword());
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al registrar el usuario: " + e.getMessage());
        }
    }
    //TODO: implement refreshToken in the future
}