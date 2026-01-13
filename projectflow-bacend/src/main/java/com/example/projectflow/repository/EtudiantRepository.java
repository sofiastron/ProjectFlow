package com.example.projectflow.repository;

import com.example.projectflow.entity.Etudiant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EtudiantRepository extends JpaRepository<Etudiant, Long> {
    
    // Recherche par CIN
    Optional<Etudiant> findByCin(String cin);
    
    // Recherche par email
    Optional<Etudiant> findByEmail(String email);
    
    // Recherche par filière
    List<Etudiant> findByFiliere(String filiere);
    
    // Étudiants sans encadrant
    @Query("SELECT e FROM Etudiant e WHERE e.professeur IS NULL")
    List<Etudiant> findEtudiantsSansEncadrant();
    
    // Étudiants sans encadrant par filière
    @Query("SELECT e FROM Etudiant e WHERE e.professeur IS NULL AND e.filiere = :filiere")
    List<Etudiant> findEtudiantsSansEncadrantByFiliere(@Param("filiere") String filiere);
    
    // Étudiants d'un professeur spécifique
    @Query("SELECT e FROM Etudiant e WHERE e.professeur.id = :professeurId")
    List<Etudiant> findByProfesseurId(@Param("professeurId") Long professeurId);
    
    // Compte des étudiants par filière
    @Query("SELECT e.filiere, COUNT(e) FROM Etudiant e GROUP BY e.filiere")
    List<Object[]> countByFiliere();
    
    // Liste des filières distinctes
    @Query("SELECT DISTINCT e.filiere FROM Etudiant e ORDER BY e.filiere")
    List<String> findDistinctFilieres();
}