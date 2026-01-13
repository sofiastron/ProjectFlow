package com.example.projectflow.DTOs;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TacheDTO {
    private Integer id;
    private String titre;
    private String description;
    private Integer dureeJours;
    private Date dateDebut;
    private Date dateFin;
    private String etat;
    private Integer sujetId;
}