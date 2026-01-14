package com.example.projectflow.CONTROLLERS;

import com.example.projectflow.DTOs.LoginDTO;
import com.example.projectflow.DTOs.LoginResponse;
import com.example.projectflow.DTOs.RegisterDTO;
import com.example.projectflow.SERVICES.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")

@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginDTO loginDTO) {
        try {
            LoginResponse response = authService.login(loginDTO);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    @PostMapping("/register")
    public ResponseEntity<LoginResponse> register(@RequestBody RegisterDTO registerDTO) {
        try {
            LoginResponse response = authService.register(registerDTO);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

}