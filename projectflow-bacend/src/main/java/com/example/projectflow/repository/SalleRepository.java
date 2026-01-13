package com.example.projectflow.repository;

import com.example.projectflow.entity.Salle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SalleRepository extends JpaRepository<Salle, Long> {
    
    // Recherche par nom
    Optional<Salle> findByNom(String nom);
    
    // Salles disponibles
    List<Salle> findByDisponibleTrue();
    
    // Salles par bâtiment
    List<Salle> findByBatiment(String batiment);
    
    // Salles avec capacité minimale
    @Query("SELECT s FROM Salle s WHERE s.capacite >= :capaciteMin AND s.disponible = true")
    List<Salle> findByCapaciteMinimale(@Param("capaciteMin") Integer capaciteMin);
    
    // Liste des bâtiments distincts
    @Query("SELECT DISTINCT s.batiment FROM Salle s WHERE s.batiment IS NOT NULL ORDER BY s.batiment")
    List<String> findDistinctBatiments();
    
    // Toutes les salles triées par nom
    List<Salle> findAllByOrderByNomAsc();
}