package com.example.projectflow.DTOs;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SujetSimpleDTO {
    private Integer id;
    private String titre;
    private String description;
}