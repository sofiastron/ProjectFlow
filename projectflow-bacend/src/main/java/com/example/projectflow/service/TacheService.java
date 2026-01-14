package com.example.projectflow.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.projectflow.entity.Tache;
import com.example.projectflow.repository.TacheRepository;

@Service
public class TacheService {

    @Autowired
    private TacheRepository tacheRepo;

    public List<Tache> getTachesByEtudiant(Long etudiantId) {
        return tacheRepo.findBySujetEtudiantIdAndEtatNotAndDateFinBefore(
                etudiantId, 
                "Terminé", 
                LocalDate.now()
        );
    }

    public List<Tache> getTachesEnCours(Long etudiantId) {
        return tacheRepo.findBySujetEtudiantIdAndEtatNotAndDateFinBefore(
                etudiantId, 
                "En retard", 
                LocalDate.now().plusDays(1000)
        );
    }
}