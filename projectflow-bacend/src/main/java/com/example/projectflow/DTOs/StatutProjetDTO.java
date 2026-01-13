package com.example.projectflow.DTOs;



import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StatutProjetDTO {
    private String type;
    private Integer count;
    private Double pourcentage;
    private String colorClass;
}


