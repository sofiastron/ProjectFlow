package com.example.projectflow.repository;

import com.example.projectflow.entity.Rapport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RapportRepository extends JpaRepository<Rapport, Long> {

	// Pour les compteurs du Dashboard (Total, Validés, En cours, À corriger)
	long countByStatut(String statut);

	// Pour l'espace Professeur : voir les rapports de ses étudiants
	List<Rapport> findByProfesseurId(Long professeurId);

	// Pour l'Admin : filtrer par statut dans le tableau
	List<Rapport> findByStatut(String statut);
}
