package com.example.projectflow.entity;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "etudiant")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true, exclude = "professeur") // Évite la boucle dans les logs
@EqualsAndHashCode(callSuper = true, exclude = "professeur") // Évite la boucle de calcul de hash
@PrimaryKeyJoinColumn(name = "id")
public class Etudiant extends User {

	@Column(length = 100)
	private String filiere;

	@Column(unique = true, length = 20)
	private String cin;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "professeur_id")
    /* On ignore 'etudiants' et 'sujets' du professeur pour que le JSON
       affiche le prof sans repartir dans une boucle infinie.
    */
	@JsonIgnoreProperties({"etudiants", "sujets", "handler", "hibernateLazyInitializer"})
	private Professeur professeur;


	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id", referencedColumnName = "etudiant_id", insertable = false, updatable = false)
	private Sujet sujet;


}
