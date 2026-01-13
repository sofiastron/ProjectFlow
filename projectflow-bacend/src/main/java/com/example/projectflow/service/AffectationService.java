package com.example.projectflow.service;


import com.example.projectflow.dto.AffectationDTO;
import com.example.projectflow.dto.EncadrantChargeDTO;
import com.example.projectflow.entity.Etudiant;
import com.example.projectflow.entity.Professeur;
import com.example.projectflow.repository.EtudiantRepository;
import com.example.projectflow.repository.ProfesseurRepository;
import com.example.projectflow.repository.ParametreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AffectationService {
    
    private final EtudiantRepository etudiantRepository;
    private final ProfesseurRepository professeurRepository;
    private final ParametreRepository parametreRepository;
    
    /**
     * Affectation manuelle d'un étudiant à un encadrant
     */
    @Transactional
    public AffectationDTO affecterManuellement(Long etudiantId, Long encadrantId) {
        
        // Vérifications
        Etudiant etudiant = etudiantRepository.findById(etudiantId)
            .orElseThrow(() -> new RuntimeException("Étudiant non trouvé"));
        
        Professeur encadrant = professeurRepository.findById(encadrantId)
            .orElseThrow(() -> new RuntimeException("Encadrant non trouvé"));
        
        // Vérifier si déjà affecté
        if (etudiant.getProfesseur() != null) {
            throw new RuntimeException("Étudiant déjà affecté à " + etudiant.getProfesseur().getNom());
        }
        
        // Vérifier capacité
        Integer capaciteMax = getCapaciteMax();
        if (!encadrant.estDisponible()) {
            throw new RuntimeException("Encadrant saturé (max: " + capaciteMax + " étudiants)");
        }
        
        // Affectation
        etudiant.setProfesseur(encadrant);
        etudiantRepository.save(etudiant);
        
        return mapToDTO(etudiant, "Manuel");
    }
    
    /**
     * Affectation automatique avec répartition équitable
     */
    @Transactional
    public AffectationDTO.ResultatAffectationAuto affecterAutomatiquement(
            AffectationDTO.AffectationAutomatiqueRequest request) {
        
        // Récupérer les étudiants sans encadrant
        List<Etudiant> etudiants = request.getFiliere() != null 
            ? etudiantRepository.findEtudiantsSansEncadrantByFiliere(request.getFiliere())
            : etudiantRepository.findEtudiantsSansEncadrant();
        
        if (etudiants.isEmpty()) {
            return new AffectationDTO.ResultatAffectationAuto(
                0, 0, "Aucun étudiant sans encadrant trouvé", new ArrayList<>()
            );
        }
        
        // Récupérer les encadrants disponibles
        Integer capaciteMax = request.getCapaciteMaxParEncadrant() != null 
            ? request.getCapaciteMaxParEncadrant() 
            : getCapaciteMax();
        
        List<Professeur> encadrants = professeurRepository.findProfesseursDisponibles(capaciteMax);
        
        if (encadrants.isEmpty()) {
            return new AffectationDTO.ResultatAffectationAuto(
                0, etudiants.size(), 
                "Aucun encadrant disponible", 
                Collections.singletonList("Tous les encadrants sont saturés")
            );
        }
        
        // Algorithme de répartition
        int nombreAffectations = 0;
        int nombreEchecs = 0;
        List<String> detailsEchecs = new ArrayList<>();
        
        if (request.getRepartitionEquitable()) {
            // Mode équitable : rotation circulaire
            Collections.shuffle(etudiants); // Randomiser pour équité
            int encadrantIndex = 0;
            
            for (Etudiant etudiant : etudiants) {
                Professeur encadrant = encadrants.get(encadrantIndex);
                
                // Vérifier si encadrant peut encore prendre des étudiants
                if (encadrant.getNombreEtudiants() < capaciteMax) {
                    etudiant.setProfesseur(encadrant);
                    etudiantRepository.save(etudiant);
                    nombreAffectations++;
                    
                    // Passer au prochain encadrant
                    encadrantIndex = (encadrantIndex + 1) % encadrants.size();
                } else {
                    // Encadrant saturé, chercher le prochain disponible
                    boolean affecte = false;
                    for (int i = 0; i < encadrants.size(); i++) {
                        Professeur autreEncadrant = encadrants.get((encadrantIndex + i) % encadrants.size());
                        if (autreEncadrant.getNombreEtudiants() < capaciteMax) {
                            etudiant.setProfesseur(autreEncadrant);
                            etudiantRepository.save(etudiant);
                            nombreAffectations++;
                            affecte = true;
                            encadrantIndex = (encadrantIndex + i + 1) % encadrants.size();
                            break;
                        }
                    }
                    
                    if (!affecte) {
                        nombreEchecs++;
                        detailsEchecs.add("Étudiant " + etudiant.getNom() + " : Aucun encadrant disponible");
                    }
                }
            }
        } else {
            // Mode aléatoire simple
            Random random = new Random();
            for (Etudiant etudiant : etudiants) {
                List<Professeur> disponibles = encadrants.stream()
                    .filter(p -> p.getNombreEtudiants() < capaciteMax)
                    .collect(Collectors.toList());
                
                if (!disponibles.isEmpty()) {
                    Professeur encadrant = disponibles.get(random.nextInt(disponibles.size()));
                    etudiant.setProfesseur(encadrant);
                    etudiantRepository.save(etudiant);
                    nombreAffectations++;
                } else {
                    nombreEchecs++;
                    detailsEchecs.add("Étudiant " + etudiant.getNom() + " : Aucun encadrant disponible");
                }
            }
        }
        
        String message = String.format(
            "%d affectations réussies, %d échecs", 
            nombreAffectations, nombreEchecs
        );
        
        return new AffectationDTO.ResultatAffectationAuto(
            nombreAffectations, nombreEchecs, message, detailsEchecs
        );
    }
    
    /**
     * Obtenir la charge de tous les encadrants
     */
    public List<EncadrantChargeDTO> getChargeEncadrants() {
        Integer capaciteMax = getCapaciteMax();
        
        return professeurRepository.findAll().stream()
            .map(p -> new EncadrantChargeDTO(
                p.getId(),
                p.getNom(),
                p.getEmail(),
                p.getSpecialite(),
                p.getNombreEtudiants(),
                capaciteMax
            ))
            .sorted(Comparator.comparing(EncadrantChargeDTO::getTauxCharge).reversed())
            .collect(Collectors.toList());
    }
    
    /**
     * Filtrer encadrants par filière (basé sur spécialité)
     */
    public List<EncadrantChargeDTO> getEncadrantsByFiliere(String filiere) {
        // Logique métier : adapter selon vos règles
        return getChargeEncadrants();
    }
    
    /**
     * Désaffecter un étudiant
     */
    @Transactional
    public void desaffecter(Long etudiantId) {
        Etudiant etudiant = etudiantRepository.findById(etudiantId)
            .orElseThrow(() -> new RuntimeException("Étudiant non trouvé"));
        
        etudiant.setProfesseur(null);
        etudiantRepository.save(etudiant);
    }
    
    /**
     * Obtenir toutes les affectations
     */
    public List<AffectationDTO> getAllAffectations() {
        return etudiantRepository.findAll().stream()
            .filter(e -> e.getProfesseur() != null)
            .map(e -> mapToDTO(e, "Existant"))
            .collect(Collectors.toList());
    }
    
    /**
     * Obtenir affectations par filière
     */
    public List<AffectationDTO> getAffectationsByFiliere(String filiere) {
        return etudiantRepository.findByFiliere(filiere).stream()
            .filter(e -> e.getProfesseur() != null)
            .map(e -> mapToDTO(e, "Existant"))
            .collect(Collectors.toList());
    }
    
    // Helper methods
    
    private Integer getCapaciteMax() {
        return parametreRepository.findValeurByCle("capacite_max_encadrant")
            .map(Integer::parseInt)
            .orElse(5); // Défaut: 5 étudiants max
    }
    
    private AffectationDTO mapToDTO(Etudiant etudiant, String modeAffectation) {
        AffectationDTO dto = new AffectationDTO();
        dto.setEtudiantId(etudiant.getId());
        dto.setEtudiantNom(etudiant.getNom());
        dto.setEtudiantEmail(etudiant.getEmail());
        dto.setEtudiantCin(etudiant.getCin());
        dto.setFiliere(etudiant.getFiliere());
        
        if (etudiant.getProfesseur() != null) {
            dto.setEncadrantId(etudiant.getProfesseur().getId());
            dto.setEncadrantNom(etudiant.getProfesseur().getNom());
            dto.setEncadrantEmail(etudiant.getProfesseur().getEmail());
            dto.setEncadrantSpecialite(etudiant.getProfesseur().getSpecialite());
        }
        
        dto.setModeAffectation(modeAffectation);
        dto.setStatut("Actif");
        
        return dto;
    }
}