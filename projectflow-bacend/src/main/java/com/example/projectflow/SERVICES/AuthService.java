package com.example.projectflow.SERVICES;
import com.example.projectflow.DTOs.LoginDTO;
import com.example.projectflow.DTOs.LoginResponse;
import com.example.projectflow.REPOSITORIES.UserRepository;
import com.example.projectflow.SECURITY.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;

public interface AuthService {
    LoginResponse login(LoginDTO loginDTO);

}