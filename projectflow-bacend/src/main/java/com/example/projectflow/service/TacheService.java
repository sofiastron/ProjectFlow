package com.example.projectflow.service;

import com.example.projectflow.model.Tache;           // ✅ AJOUTER
import com.example.projectflow.model.Sujet;           // ✅ AJOUTER
import com.example.projectflow.model.EtatTache;       // ✅ AJOUTER
import com.example.projectflow.model.Professeur;      // ✅ AJOUTER
import com.example.projectflow.model.Etudiant;        // ✅ AJOUTER
import com.example.projectflow.DTOs.TacheDTO;
import com.example.projectflow.repository.TacheRepository;
import com.example.projectflow.repository.SujetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TacheService {

    @Autowired
    private TacheRepository tacheRepository;

    @Autowired
    private SujetRepository sujetRepository;

    @Autowired
    private NotificationService notificationService;

    // Récupérer toutes les tâches d'un sujet (pour le Kanban)
    public Map<String, List<TacheDTO>> getTachesKanban(Integer sujetId) {
        List<Tache> taches = tacheRepository.findBySujetId(sujetId);

        Map<String, List<TacheDTO>> kanban = new HashMap<>();
        kanban.put("A_FAIRE", new ArrayList<>());
        kanban.put("EN_COURS", new ArrayList<>());
        kanban.put("A_VERIFIER", new ArrayList<>());
        kanban.put("TERMINE", new ArrayList<>());

        for (Tache tache : taches) {
            TacheDTO dto = convertToDTO(tache);
            String etat = tache.getEtat().toString();
            kanban.get(etat).add(dto);
        }

        return kanban;
    }

    // Récupérer toutes les tâches
    public List<TacheDTO> getAllTaches() {
        return tacheRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Récupérer une tâche par ID
    public TacheDTO getTacheById(Integer id) {
        Tache tache = tacheRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tâche non trouvée avec l'ID: " + id));
        return convertToDTO(tache);
    }

    // Créer une tâche
    public TacheDTO createTache(TacheDTO tacheDTO) {
        Sujet sujet = sujetRepository.findById(tacheDTO.getSujetId())
                .orElseThrow(() -> new RuntimeException("Sujet non trouvé"));

        Tache tache = new Tache();
        tache.setTitre(tacheDTO.getTitre());
        tache.setDescription(tacheDTO.getDescription());
        tache.setDureeJours(tacheDTO.getDureeJours());
        tache.setDateDebut(tacheDTO.getDateDebut());
        tache.setDateFin(tacheDTO.getDateFin());
        tache.setEtat(EtatTache.valueOf(tacheDTO.getEtat()));
        tache.setSujet(sujet);

        Tache savedTache = tacheRepository.save(tache);
        return convertToDTO(savedTache);
    }

    // Mettre à jour une tâche
    public TacheDTO updateTache(Integer id, TacheDTO tacheDTO) {
        Tache tache = tacheRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tâche non trouvée"));

        tache.setTitre(tacheDTO.getTitre());
        tache.setDescription(tacheDTO.getDescription());
        tache.setDureeJours(tacheDTO.getDureeJours());
        tache.setDateDebut(tacheDTO.getDateDebut());
        tache.setDateFin(tacheDTO.getDateFin());

        Tache savedTache = tacheRepository.save(tache);
        return convertToDTO(savedTache);
    }

    // Changer l'état d'une tâche
    @Transactional
    public TacheDTO changerEtat(Integer tacheId, String nouvelEtat) {
        Tache tache = tacheRepository.findById(tacheId)
                .orElseThrow(() -> new RuntimeException("Tâche non trouvée avec l'ID: " + tacheId));

        EtatTache ancienEtat = tache.getEtat();
        EtatTache newEtat = EtatTache.valueOf(nouvelEtat);

        tache.setEtat(newEtat);

        // Si la tâche passe à "A_VERIFIER", créer une notification pour le professeur
        if (newEtat == EtatTache.A_VERIFIER && ancienEtat != EtatTache.A_VERIFIER) {
            Sujet sujet = tache.getSujet();
            Professeur prof = sujet.getProfesseur();
            Etudiant etudiant = sujet.getEtudiant();

            String message = "L'étudiant " + etudiant.getNom() +
                    " a soumis la tâche '" + tache.getTitre() +
                    "' pour vérification";

            notificationService.creerNotification(prof.getId(), message, tache);
        }

        Tache savedTache = tacheRepository.save(tache);
        return convertToDTO(savedTache);
    }

    // Valider une tâche (professeur clique sur "Vérifier")
    @Transactional
    public TacheDTO validerTache(Integer tacheId) {
        Tache tache = tacheRepository.findById(tacheId)
                .orElseThrow(() -> new RuntimeException("Tâche non trouvée avec l'ID: " + tacheId));

        if (tache.getEtat() != EtatTache.A_VERIFIER) {
            throw new RuntimeException("Cette tâche n'est pas en attente de vérification");
        }

        tache.setEtat(EtatTache.TERMINE);
        Tache savedTache = tacheRepository.save(tache);

        // Notifier l'étudiant que sa tâche est validée
        Etudiant etudiant = tache.getSujet().getEtudiant();
        String message = "Votre tâche '" + tache.getTitre() + "' a été validée par le professeur";
        notificationService.creerNotification(etudiant.getId(), message, tache);

        return convertToDTO(savedTache);
    }

    // Supprimer une tâche
    public void deleteTache(Integer id) {
        Tache tache = tacheRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tâche non trouvée"));
        tacheRepository.delete(tache);
    }

    // Conversion vers DTO
    private TacheDTO convertToDTO(Tache tache) {
        TacheDTO dto = new TacheDTO();
        dto.setId(tache.getId());
        dto.setTitre(tache.getTitre());
        dto.setDescription(tache.getDescription());
        dto.setDureeJours(tache.getDureeJours());
        dto.setDateDebut(tache.getDateDebut());
        dto.setDateFin(tache.getDateFin());
        dto.setEtat(tache.getEtat().toString());
        dto.setSujetId(tache.getSujet().getId());
        return dto;
    }
}