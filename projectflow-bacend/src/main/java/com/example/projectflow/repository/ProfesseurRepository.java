package com.example.projectflow.repository;

import com.example.projectflow.entity.Professeur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfesseurRepository extends JpaRepository<Professeur, Long> {
    
    // Recherche par email
    Optional<Professeur> findByEmail(String email);
    
    // Recherche par spécialité
    List<Professeur> findBySpecialite(String specialite);
    
    // Professeurs disponibles (avec moins de X étudiants)
    @Query("SELECT p FROM Professeur p WHERE SIZE(p.etudiants) < :capaciteMax")
    List<Professeur> findProfesseursDisponibles(@Param("capaciteMax") Integer capaciteMax);
    
    // Professeurs avec leur charge
    @Query("SELECT p, SIZE(p.etudiants) as nbEtudiants FROM Professeur p ORDER BY nbEtudiants ASC")
    List<Object[]> findAllWithChargeOrderByChargeAsc();
    
    // Professeurs surchargés
    @Query("SELECT p FROM Professeur p WHERE SIZE(p.etudiants) >= :capaciteMax")
    List<Professeur> findProfesseursSurcharges(@Param("capaciteMax") Integer capaciteMax);
    
    // Nombre d'étudiants par professeur
    @Query("SELECT p.id, p.nom, COUNT(e) FROM Professeur p LEFT JOIN p.etudiants e GROUP BY p.id, p.nom")
    List<Object[]> countEtudiantsByProfesseur();
    
    // Professeur le moins chargé
    @Query("SELECT p FROM Professeur p ORDER BY SIZE(p.etudiants) ASC")
    List<Professeur> findOrderByChargeCroissante();
    
    // Liste des spécialités distinctes
    @Query("SELECT DISTINCT p.specialite FROM Professeur p ORDER BY p.specialite")
    List<String> findDistinctSpecialites();
}