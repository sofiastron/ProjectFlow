package com.example.projectflow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TacheDTO {
    private Long id;
    private String titre;
    private String description;
    private String etat;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private int dureeJours;
    private String projetTitre; // Nom du projet
}
