package com.example.projectflow.DTOs;

import lombok.*;

@Data
@Builder
public class ActiviteJourDTO {
    private String jour;
    private Integer valeur;
}