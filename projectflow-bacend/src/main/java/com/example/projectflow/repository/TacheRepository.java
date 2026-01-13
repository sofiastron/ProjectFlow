package com.example.projectflow.repository;

import com.example.projectflow.model.Tache;
import com.example.projectflow.model.EtatTache;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TacheRepository extends JpaRepository<Tache, Integer> {

    // Trouver toutes les tâches d'un sujet
    List<Tache> findBySujetId(Integer sujetId);

    // Trouver les tâches par état
    List<Tache> findBySujetIdAndEtat(Integer sujetId, EtatTache etat);

    // Compter les tâches par état
    long countBySujetIdAndEtat(Integer sujetId, EtatTache etat);
}