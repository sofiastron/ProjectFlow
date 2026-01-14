package com.example.projectflow.entity;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
// import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@Entity
@Table(name = "professeur")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = {"etudiants", "sujets"}) // Exclure pour éviter les boucles de hash
@ToString(exclude = {"etudiants", "sujets"}) // Exclure pour éviter les boucles dans les logs
@PrimaryKeyJoinColumn(name = "id")
public class Professeur extends User {

	@Column(length = 100)
	private String specialite;

	@OneToMany(mappedBy = "professeur", fetch = FetchType.LAZY)
	@JsonIgnore // Empêche Jackson de charger tous les étudiants quand on regarde un prof
	private List<Etudiant> etudiants;

	@OneToMany(mappedBy = "professeur", fetch = FetchType.LAZY)
	@JsonIgnore // Empêche Jackson de charger tous les sujets
	private List<Sujet> sujets;

	@Transient
	private Integer capaciteMax = 5;

	// Note: Ces méthodes peuvent causer une erreur 500 si appelées
	// alors que la session Hibernate est fermée (LazyInitializationException)
	@Transient
	public Integer getNombreEtudiants() {
		return (etudiants != null) ? etudiants.size() : 0;
	}

	@Transient
	public Boolean estDisponible() {
		return getNombreEtudiants() < capaciteMax;
	}

	@Transient
	public Double getTauxCharge() {
		return (capaciteMax > 0) ? (getNombreEtudiants().doubleValue() / capaciteMax) * 100 : 0.0;
	}
}
