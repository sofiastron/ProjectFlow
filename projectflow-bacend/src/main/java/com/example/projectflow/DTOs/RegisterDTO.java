package com.example.projectflow.DTOs;

import lombok.Data;

@Data
public class RegisterDTO {
    private String nom;
    private String email;
    private String password;
    private String role; // Professeur ou Etudiant
}
