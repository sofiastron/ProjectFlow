package com.example.projectflow.entity;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDate;

@Entity
@Table(name = "rapport")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Rapport {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String titre;

	@Column(name = "fichier_path")
	private String fichierPath;

	@Column(name = "date_depot")
	private LocalDate dateDepot;

	@Column(nullable = false)
	private String statut = "En cours";

	// Liaison avec l'Étudiant (Clé MUL dans votre base)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "etudiant_id", nullable = false)
	@JsonIgnoreProperties({"rapports", "professeur", "handler", "hibernateLazyInitializer"})
	private Etudiant etudiant;

	// Liaison avec le Professeur/Correcteur (Clé MUL dans votre base)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "professeur_id", nullable = false)
	@JsonIgnoreProperties({"rapports", "etudiants", "handler", "hibernateLazyInitializer"})
	private Professeur professeur;

	// Liaison avec le Sujet (Changé en ManyToOne pour être compatible avec votre index MUL)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sujet_id")
	@JsonIgnoreProperties({"rapports", "handler", "hibernateLazyInitializer"})
	private Sujet sujet;
	// Ajoutez ceci juste avant l'accolade de fin de votre classe Rapport
	public void setStatut(String statut) {
		this.statut = statut;
	}
}
