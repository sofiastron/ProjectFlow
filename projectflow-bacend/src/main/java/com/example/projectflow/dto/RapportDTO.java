package com.example.projectflow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RapportDTO {
	private Long id;
	private String titre;
	private String fichierPath;
	private LocalDate dateDepot;
	private String statut;

	// Informations simplifiées pour l'affichage dans le tableau
	private String nomEtudiant;   // Récupéré depuis User (via Etudiant)
	private String nomProfesseur; // Récupéré depuis User (via Professeur)
	private String titreSujet;    // Récupéré depuis l'entité Sujet
}
