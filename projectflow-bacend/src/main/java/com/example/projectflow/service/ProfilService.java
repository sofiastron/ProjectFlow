package com.example.projectflow.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.projectflow.entity.Professeur;
import com.example.projectflow.entity.etudiant;
import com.example.projectflow.repository.EtudiantRepository;
import com.example.projectflow.repository.ProfesseurRepository;

@Service
public class ProfilService {

    @Autowired
    private EtudiantRepository etudiantRepository;

    @Autowired
    private ProfesseurRepository professeurRepository;

    public Optional<etudiant> getProfilEtudiant(Long etudiantId) {
        return etudiantRepository.findById(etudiantId);
    }

    public etudiant updateProfilEtudiant(Long etudiantId, etudiant updatedEtudiant) {
        Optional<etudiant> opt = etudiantRepository.findById(etudiantId);
        if (opt.isPresent()) {
            etudiant etud = opt.get();
            etud.setFiliere(updatedEtudiant.getFiliere());
            etud.setCin(updatedEtudiant.getCin());
            etud.setProfesseur(updatedEtudiant.getProfesseur());
            return etudiantRepository.save(etud);
        }
        return null;
    }

  
    public Optional<Professeur> getProfilProfesseur(Long professeurId) {
        return professeurRepository.findById(professeurId);
    }

   
    public Professeur updateProfilProfesseur(Long professeurId, Professeur updatedProfesseur) {
        Optional<Professeur> opt = professeurRepository.findById(professeurId);
        if (opt.isPresent()) {
            Professeur prof = opt.get();
            prof.setSpecialite(updatedProfesseur.getSpecialite());
            return professeurRepository.save(prof);
        }
        return null;
    }
}
