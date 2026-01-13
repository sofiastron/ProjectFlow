package com.example.projectflow.DTOs;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EtudiantWithSujetDTO {
    private Integer id;
    private String nom;
    private String email;
    private String filiere;
    private String cin;
    private SujetSimpleDTO sujet;
}