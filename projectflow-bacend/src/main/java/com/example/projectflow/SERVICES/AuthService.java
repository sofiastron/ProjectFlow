package com.example.projectflow.SERVICES;

import com.example.projectflow.DTOs.LoginDTO;
import com.example.projectflow.DTOs.LoginResponse;

public interface AuthService {
    LoginResponse login(LoginDTO loginDTO);
}