package com.example.projectflow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjetDTO {
    private Long id;
    private String titre;
    private String description;
    private String encadrantNom;
    private int progression; // en %
}
