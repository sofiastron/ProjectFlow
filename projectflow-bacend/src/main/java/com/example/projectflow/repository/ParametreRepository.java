package com.example.projectflow.repository;

import com.example.projectflow.entity.Parametre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParametreRepository extends JpaRepository<Parametre, Long> {
    
    // Recherche par clé
    Optional<Parametre> findByCle(String cle);
    
    // Paramètres par type
    List<Parametre> findByType(Parametre.TypeParametre type);
    
    // Tous les paramètres triés par type puis clé
    @Query("SELECT p FROM Parametre p ORDER BY p.type, p.cle")
    List<Parametre> findAllOrderedByTypeAndCle();
    
    // Vérifier l'existence d'une clé
    Boolean existsByCle(String cle);
    
    // Récupérer valeur par clé (helper)
    @Query("SELECT p.valeur FROM Parametre p WHERE p.cle = :cle")
    Optional<String> findValeurByCle(@Param("cle") String cle);
}