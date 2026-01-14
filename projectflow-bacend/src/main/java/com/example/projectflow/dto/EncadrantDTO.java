package com.example.projectflow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EncadrantDTO {
	private Long id;
	private String nom;
	private String email;
	private String specialite;
	private Integer nombreEtudiantsActuels;
	private Integer capaciteMax;
	private Double tauxCharge;
}
