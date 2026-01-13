package com.example.projectflow.repository;

import com.example.projectflow.entity.Soutenance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SoutenanceRepository extends JpaRepository<Soutenance, Long> {
    
    // Recherche par étudiant
    Optional<Soutenance> findByEtudiantId(Long etudiantId);
    
    // Soutenances par date
    List<Soutenance> findByDate(LocalDate date);
    
    // Soutenances par salle
    List<Soutenance> findBySalle(String salle);
    
    // Soutenances entre deux dates
    @Query("SELECT s FROM Soutenance s WHERE s.date BETWEEN :dateDebut AND :dateFin ORDER BY s.date, s.heure")
    List<Soutenance> findByDateBetween(@Param("dateDebut") LocalDate dateDebut, 
                                       @Param("dateFin") LocalDate dateFin);
    
    // Vérifier disponibilité salle
    @Query("SELECT COUNT(s) > 0 FROM Soutenance s WHERE s.salle = :salle AND s.date = :date AND s.heure = :heure")
    Boolean isSalleOccupee(@Param("salle") String salle, 
                          @Param("date") LocalDate date, 
                          @Param("heure") LocalTime heure);
    
    // Soutenances d'un professeur (en tant qu'encadrant ou jury)
    @Query("SELECT s FROM Soutenance s WHERE s.professeur.id = :professeurId " +
           "OR s.president.id = :professeurId " +
           "OR s.rapporteur.id = :professeurId " +
           "OR s.examinateur.id = :professeurId")
    List<Soutenance> findByProfesseurId(@Param("professeurId") Long professeurId);
    
    // Soutenances à venir
    @Query("SELECT s FROM Soutenance s WHERE s.date >= :today ORDER BY s.date, s.heure")
    List<Soutenance> findSoutenancesAVenir(@Param("today") LocalDate today);
    
    // Soutenances passées
    @Query("SELECT s FROM Soutenance s WHERE s.date < :today ORDER BY s.date DESC, s.heure DESC")
    List<Soutenance> findSoutenancesPassees(@Param("today") LocalDate today);
    
    // Soutenances par salle et date
    @Query("SELECT s FROM Soutenance s WHERE s.salle = :salle AND s.date = :date ORDER BY s.heure")
    List<Soutenance> findBySalleAndDate(@Param("salle") String salle, @Param("date") LocalDate date);
    
    // Statistiques
    @Query("SELECT COUNT(s), AVG(s.note) FROM Soutenance s WHERE s.note IS NOT NULL")
    Object[] getStatistiques();
}