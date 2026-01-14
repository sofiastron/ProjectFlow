package com.example.projectflow.SERVICES;

import com.example.projectflow.DTOs.LoginDTO;
import com.example.projectflow.DTOs.LoginResponse;
import com.example.projectflow.DTOs.RegisterDTO;


public interface AuthService {
    LoginResponse login(LoginDTO loginDTO);
    LoginResponse register(RegisterDTO registerDTO);
}