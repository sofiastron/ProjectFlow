package com.example.projectflow.DTOs;


import jdk.jshell.Snippet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfesseurProfileDTO {
    private Integer id;
    private String nom;
    private String email;
    private String specialite;
    private String role;
    private LocalDateTime createdAt;
    private String photo;



    public String getNom() {
        return nom;
    }
    public void setNom(String nom){
        this.nom=nom;
    }

    public String getEmail() {
        return email;
    }

    public String getSpecialite() {
        return this.specialite;
    }
}