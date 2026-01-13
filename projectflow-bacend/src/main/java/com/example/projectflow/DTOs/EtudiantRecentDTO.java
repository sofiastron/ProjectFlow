package com.example.projectflow.DTOs;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EtudiantRecentDTO {
    private Integer id;
    private String nom;
    private String initiales;
    private String filiere;
    private String dateAssignation;
}