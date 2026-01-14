package com.example.projectflow.DTOs;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EtudiantDTO {
    private Integer id;
    private String nom;
    private String email;
    private String filiere;
    private String cin;
    private Boolean hasProjet;
    private String projetTitre;
    private String dateAssignation;
}