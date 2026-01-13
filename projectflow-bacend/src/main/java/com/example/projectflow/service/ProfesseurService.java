package com.example.projectflow.service;

import com.example.projectflow.model.Professeur;
import com.example.projectflow.repository.ProfesseurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProfesseurService {

    @Autowired
    private ProfesseurRepository professeurRepository;

    // Récupérer tous les professeurs
    public List<Professeur> getAllProfesseurs() {
        return professeurRepository.findAll();
    }

    // Récupérer un professeur par ID
    public Professeur getProfesseurById(Integer id) {
        return professeurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Professeur non trouvé avec l'ID: " + id));
    }

    // Créer un professeur
    public Professeur createProfesseur(Professeur professeur) {
        return professeurRepository.save(professeur);
    }

    // Mettre à jour un professeur
    public Professeur updateProfesseur(Integer id, Professeur professeurDetails) {
        Professeur professeur = getProfesseurById(id);

        professeur.setNom(professeurDetails.getNom());
        professeur.setEmail(professeurDetails.getEmail());
        professeur.setSpecialite(professeurDetails.getSpecialite());

        if (professeurDetails.getPassword() != null && !professeurDetails.getPassword().isEmpty()) {
            professeur.setPassword(professeurDetails.getPassword());
        }

        return professeurRepository.save(professeur);
    }

    // Supprimer un professeur
    public void deleteProfesseur(Integer id) {
        Professeur professeur = getProfesseurById(id);
        professeurRepository.delete(professeur);
    }
}