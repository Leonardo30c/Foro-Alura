package com.foroalura.controller;

import com.foroalura.model.Usuario;
import com.foroalura.security.JwtUtil;
import com.foroalura.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.Data;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UsuarioService usuarioService;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, @Lazy UsuarioService usuarioService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.usuarioService = usuarioService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthRequest request) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            String token = jwtUtil.generateToken(request.getEmail());
            return ResponseEntity.ok(new AuthResponse(token));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body("Credenciales inválidas");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error interno del servidor");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterRequest request) {
        try {
            Usuario usuario = new Usuario();
            usuario.setNombre(request.getNombre());
            usuario.setEmail(request.getEmail());
            usuario.setPassword(request.getPassword());
            // Eliminé esta línea porque no existe setRol en Usuario:
            // usuario.setRol("USER");

            usuario = usuarioService.crearUsuario(usuario);
            return ResponseEntity.status(201).body("Usuario registrado: " + usuario.getEmail());
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Error al registrar usuario: " + e.getMessage());
        }
    }

    @Data
    static class AuthRequest {
        private String email;
        private String password;
    }

    @Data
    static class RegisterRequest {
        private String nombre;
        private String email;
        private String password;
    }

    @Data
    static class AuthResponse {
        private final String token;
    }
}
