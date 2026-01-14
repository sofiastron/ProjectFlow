package com.example.projectflow.service;

import com.example.projectflow.entity.Etudiant;
import com.example.projectflow.repository.EtudiantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EtudiantService {

    @Autowired
    private EtudiantRepository etudiantRepo;

    public List<Etudiant> getAllEtudiants() {
        return etudiantRepo.findAll();
    }

    public List<Etudiant> getEtudiantsParFiliere(String filiere) {
        return etudiantRepo.findByFiliere(filiere);
    }

    public Etudiant getEtudiantByCin(String cin) {
        return etudiantRepo.findByCin(cin).orElse(null);
    }

    public Etudiant saveEtudiant(Etudiant etudiant) {
        return etudiantRepo.save(etudiant);
    }

    public Etudiant updateEtudiant(Long id, Etudiant etudiant) {
        Etudiant existing = etudiantRepo.findById(id).orElseThrow(() -> new RuntimeException("Étudiant introuvable"));
        existing.setNom(etudiant.getNom());
        existing.setEmail(etudiant.getEmail());
        existing.setFiliere(etudiant.getFiliere());
        existing.setCin(etudiant.getCin());
        // existing.setProfesseur(etudiant.getProfesseur());
        // existing.setTelephone(etudiant.getTelephone());
        existing.setSujet(etudiant.getSujet());
        return etudiantRepo.save(existing);
    }

    public void deleteEtudiant(Long id) {
        etudiantRepo.deleteById(id);
    }
}

