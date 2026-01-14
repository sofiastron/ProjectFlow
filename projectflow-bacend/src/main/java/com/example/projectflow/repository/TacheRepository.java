package com.example.projectflow.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.projectflow.entity.Tache;

@Repository
public interface TacheRepository extends JpaRepository<Tache, Long> {

    
    List<Tache> findBySujetEtudiantIdAndEtatNotAndDateFinBefore(Long etudiantId, String etat, LocalDate date);

    
    int countBySujetEtudiantIdAndEtatNotAndDateFinBefore(Long etudiantId, String etat, LocalDate date);
}
