package com.example.projectflow.service;

import com.example.projectflow.model.Sujet;
import com.example.projectflow.model.Etudiant;
import com.example.projectflow.repository.SujetRepository;
import com.example.projectflow.repository.ProfesseurRepository;
import com.example.projectflow.repository.EtudiantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;

@Service
public class SujetService {

    @Autowired
    private SujetRepository sujetRepository;

    @Autowired
    private ProfesseurRepository professeurRepository;

    @Autowired
    private EtudiantRepository etudiantRepository;

    // Récupérer tous les sujets
    public List<Sujet> getAllSujets() {
        return sujetRepository.findAll();
    }

    // Récupérer un sujet par ID
    public Sujet getSujetById(Integer id) {
        return sujetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sujet non trouvé avec l'ID: " + id));
    }

    // Récupérer les sujets d'un professeur
    public List<Sujet> getSujetsByProfesseur(Integer professeurId) {
        return sujetRepository.findByProfesseurId(professeurId);
    }

    // Récupérer le sujet d'un étudiant
    public Sujet getSujetByEtudiant(Integer etudiantId) {
        return sujetRepository.findByEtudiantId(etudiantId);
    }

    // Créer un sujet
    public Sujet createSujet(Sujet sujet) {
        sujet.setCreatedAt(new Date());
        sujet.setUpdatedAt(new Date());
        return sujetRepository.save(sujet);
    }

    // Mettre à jour un sujet
    public Sujet updateSujet(Integer id, Sujet sujetDetails) {
        Sujet sujet = getSujetById(id);

        sujet.setTitre(sujetDetails.getTitre());
        sujet.setDescription(sujetDetails.getDescription());
        sujet.setUpdatedAt(new Date());

        return sujetRepository.save(sujet);
    }

    // Affecter un sujet à un étudiant
    public Sujet affecterSujet(Integer sujetId, Integer etudiantId) {
        Sujet sujet = getSujetById(sujetId);
        Etudiant etudiant = etudiantRepository.findById(etudiantId)
                .orElseThrow(() -> new RuntimeException("Étudiant non trouvé"));

        sujet.setEtudiant(etudiant);
        sujet.setUpdatedAt(new Date());

        return sujetRepository.save(sujet);
    }

    // Supprimer un sujet
    public void deleteSujet(Integer id) {
        Sujet sujet = getSujetById(id);
        sujetRepository.delete(sujet);
    }
}