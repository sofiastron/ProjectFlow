package com.example.projectflow.DTOs;

import lombok.*;

@Data
@Builder
public class StatutProjetDTO {
    private String type;
    private Integer count;
    private Double pourcentage;
    private String colorClass;
}