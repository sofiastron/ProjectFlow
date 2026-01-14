package com.example.projectflow.DTOs;

import lombok.*;

@Data
@Builder
public class LoginResponse {
    private String token;
    private UserDTO user;
}