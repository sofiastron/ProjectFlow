package com.example.projectflow.DTOs;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDTO {
    private Integer id;
    private String nom;
    private String email;
    private String role;
}