package com.example.projectflow.DTOs;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ActiviteJourDTO {
    private String jour;
    private Integer valeur;


}
