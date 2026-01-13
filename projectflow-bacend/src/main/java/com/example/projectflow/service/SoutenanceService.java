package com.example.projectflow.service;

import com.example.projectflow.dto.SoutenanceDTO;
import com.example.projectflow.entity.*;
import com.example.projectflow.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SoutenanceService {
    
    private final SoutenanceRepository soutenanceRepository;
    private final EtudiantRepository etudiantRepository;
    private final ProfesseurRepository professeurRepository;
    // private final SalleRepository salleRepository;
    // private final ParametreRepository parametreRepository;
    
    /**
     * Créer/Planifier une soutenance
     */
    @Transactional
    public SoutenanceDTO.PlanificationResponse planifierSoutenance(SoutenanceDTO.SoutenanceRequest request) {
        
        List<String> conflits = new ArrayList<>();
        
        // Vérifications
        Etudiant etudiant = etudiantRepository.findById(request.getEtudiantId())
            .orElseThrow(() -> new RuntimeException("Étudiant non trouvé"));
        
        // Vérifier si étudiant a déjà une soutenance
        Optional<Soutenance> existante = soutenanceRepository.findByEtudiantId(request.getEtudiantId());
        if (existante.isPresent()) {
            conflits.add("L'étudiant a déjà une soutenance planifiée");
            return new SoutenanceDTO.PlanificationResponse(
                false, "Soutenance existante", null, conflits
            );
        }
        
        // Vérifier disponibilité salle
        if (request.getSalle() != null) {
            Boolean salleOccupee = soutenanceRepository.isSalleOccupee(
                request.getSalle(), request.getDate(), request.getHeure()
            );
            if (salleOccupee) {
                conflits.add("Salle occupée à cet horaire");
            }
        }
        
        // Vérifier membres du jury
        Professeur president = null, rapporteur = null, examinateur = null;
        
        if (request.getPresidentId() != null) {
            president = professeurRepository.findById(request.getPresidentId())
                .orElseThrow(() -> new RuntimeException("Président du jury non trouvé"));
        }
        
        if (request.getRapporteurId() != null) {
            rapporteur = professeurRepository.findById(request.getRapporteurId())
                .orElseThrow(() -> new RuntimeException("Rapporteur non trouvé"));
            
            // Vérifier que le rapporteur n'est pas le même que le président
            if (request.getRapporteurId().equals(request.getPresidentId())) {
                conflits.add("Le rapporteur ne peut pas être le président");
            }
        }
        
        if (request.getExaminateurId() != null) {
            examinateur = professeurRepository.findById(request.getExaminateurId())
                .orElseThrow(() -> new RuntimeException("Examinateur non trouvé"));
            
            // Vérifier unicité
            if (request.getExaminateurId().equals(request.getPresidentId()) ||
                request.getExaminateurId().equals(request.getRapporteurId())) {
                conflits.add("L'examinateur doit être différent des autres membres");
            }
        }
        
        // Si conflits, retourner erreur
        if (!conflits.isEmpty()) {
            return new SoutenanceDTO.PlanificationResponse(
                false, "Conflits détectés", null, conflits
            );
        }
        
        // Créer la soutenance
        Soutenance soutenance = new Soutenance();
        soutenance.setEtudiant(etudiant);
        soutenance.setProfesseur(etudiant.getProfesseur()); // Encadrant
        soutenance.setDate(request.getDate());
        soutenance.setHeure(request.getHeure());
        soutenance.setSalle(request.getSalle());
        soutenance.setRapportFinal(request.getRapportFinal());
        
        // Jury
        soutenance.setPresident(president);
        soutenance.setRapporteur(rapporteur);
        soutenance.setExaminateur(examinateur);
        
        // Sauvegarder
        soutenance = soutenanceRepository.save(soutenance);
        
        return new SoutenanceDTO.PlanificationResponse(
            true, 
            "Soutenance planifiée avec succès", 
            mapToDTO(soutenance),
            new ArrayList<>()
        );
    }
    
    /**
     * Modifier une soutenance
     */
    @Transactional
    public SoutenanceDTO modifierSoutenance(Long soutenanceId, SoutenanceDTO.SoutenanceRequest request) {
        Soutenance soutenance = soutenanceRepository.findById(soutenanceId)
            .orElseThrow(() -> new RuntimeException("Soutenance non trouvée"));
        
        // Mettre à jour les champs
        if (request.getDate() != null) soutenance.setDate(request.getDate());
        if (request.getHeure() != null) soutenance.setHeure(request.getHeure());
        if (request.getSalle() != null) soutenance.setSalle(request.getSalle());
        if (request.getRapportFinal() != null) soutenance.setRapportFinal(request.getRapportFinal());
        
        // Jury
        if (request.getPresidentId() != null) {
            Professeur president = professeurRepository.findById(request.getPresidentId())
                .orElseThrow(() -> new RuntimeException("Président non trouvé"));
            soutenance.setPresident(president);
        }
        
        if (request.getRapporteurId() != null) {
            Professeur rapporteur = professeurRepository.findById(request.getRapporteurId())
                .orElseThrow(() -> new RuntimeException("Rapporteur non trouvé"));
            soutenance.setRapporteur(rapporteur);
        }
        
        if (request.getExaminateurId() != null) {
            Professeur examinateur = professeurRepository.findById(request.getExaminateurId())
                .orElseThrow(() -> new RuntimeException("Examinateur non trouvé"));
            soutenance.setExaminateur(examinateur);
        }
        
        soutenance = soutenanceRepository.save(soutenance);
        return mapToDTO(soutenance);
    }
    
    /**
     * Obtenir toutes les soutenances
     */
    public List<SoutenanceDTO> getAllSoutenances() {
        return soutenanceRepository.findAll().stream()
            .map(this::mapToDTO)
            .sorted(Comparator.comparing(SoutenanceDTO::getDate)
                             .thenComparing(SoutenanceDTO::getHeure))
            .collect(Collectors.toList());
    }
    
    /**
     * Obtenir soutenances par période
     */
    public List<SoutenanceDTO> getSoutenancesByPeriode(LocalDate dateDebut, LocalDate dateFin) {
        return soutenanceRepository.findByDateBetween(dateDebut, dateFin).stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }
    
    /**
     * Obtenir soutenances à venir
     */
    public List<SoutenanceDTO> getSoutenancesAVenir() {
        return soutenanceRepository.findSoutenancesAVenir(LocalDate.now()).stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }
    
    /**
     * Obtenir calendrier des soutenances (groupées par date)
     */
    public List<SoutenanceDTO.CalendrierSoutenanceDTO> getCalendrierSoutenances(
            LocalDate dateDebut, LocalDate dateFin) {
        
        List<Soutenance> soutenances = soutenanceRepository.findByDateBetween(dateDebut, dateFin);
        
        Map<LocalDate, List<Soutenance>> groupees = soutenances.stream()
            .collect(Collectors.groupingBy(Soutenance::getDate));
        
        return groupees.entrySet().stream()
            .map(entry -> new SoutenanceDTO.CalendrierSoutenanceDTO(
                entry.getKey(),
                entry.getValue().stream().map(this::mapToDTO).collect(Collectors.toList()),
                entry.getValue().size()
            ))
            .sorted(Comparator.comparing(SoutenanceDTO.CalendrierSoutenanceDTO::getDate))
            .collect(Collectors.toList());
    }
    
    /**
     * Obtenir soutenances par salle
     */
    public List<SoutenanceDTO> getSoutenancesBySalle(String salle, LocalDate date) {
        return soutenanceRepository.findBySalleAndDate(salle, date).stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }
    
    /**
     * Vérifier disponibilité jury (professeur)
     */
    public Boolean isJuryDisponible(Long professeurId, LocalDate date, LocalTime heure) {
        List<Soutenance> soutenances = soutenanceRepository.findByProfesseurId(professeurId);
        
        return soutenances.stream()
            .noneMatch(s -> s.getDate().equals(date) && s.getHeure().equals(heure));
    }
    
    /**
     * Supprimer une soutenance
     */
    @Transactional
    public void supprimerSoutenance(Long soutenanceId) {
        soutenanceRepository.deleteById(soutenanceId);
    }
    
    /**
     * Attribuer une note
     */
    @Transactional
    public SoutenanceDTO attribuerNote(Long soutenanceId, Float note) {
        Soutenance soutenance = soutenanceRepository.findById(soutenanceId)
            .orElseThrow(() -> new RuntimeException("Soutenance non trouvée"));
        
        if (note < 0 || note > 20) {
            throw new RuntimeException("Note invalide (0-20)");
        }
        
        soutenance.setNote(note);
        soutenance = soutenanceRepository.save(soutenance);
        
        return mapToDTO(soutenance);
    }
    
    // Helper methods
    
    private SoutenanceDTO mapToDTO(Soutenance soutenance) {
        SoutenanceDTO dto = new SoutenanceDTO();
        
        dto.setId(soutenance.getId());
        dto.setDate(soutenance.getDate());
        dto.setHeure(soutenance.getHeure());
        dto.setSalle(soutenance.getSalle());
        dto.setNote(soutenance.getNote());
        dto.setRapportFinal(soutenance.getRapportFinal());
        
        // Étudiant
        if (soutenance.getEtudiant() != null) {
            dto.setEtudiantId(soutenance.getEtudiant().getId());
            dto.setEtudiantNom(soutenance.getEtudiant().getNom());
            dto.setEtudiantEmail(soutenance.getEtudiant().getEmail());
            dto.setFiliere(soutenance.getEtudiant().getFiliere());
        }
        
        // Encadrant
        if (soutenance.getProfesseur() != null) {
            dto.setEncadrantId(soutenance.getProfesseur().getId());
            dto.setEncadrantNom(soutenance.getProfesseur().getNom());
        }
        
        // Jury
        SoutenanceDTO.JuryDTO jury = new SoutenanceDTO.JuryDTO();
        if (soutenance.getPresident() != null) {
            jury.setPresidentId(soutenance.getPresident().getId());
            jury.setPresidentNom(soutenance.getPresident().getNom());
        }
        if (soutenance.getRapporteur() != null) {
            jury.setRapporteurId(soutenance.getRapporteur().getId());
            jury.setRapporteurNom(soutenance.getRapporteur().getNom());
        }
        if (soutenance.getExaminateur() != null) {
            jury.setExaminateurId(soutenance.getExaminateur().getId());
            jury.setExaminateurNom(soutenance.getExaminateur().getNom());
        }
        dto.setJury(jury);
        
        // Statut
        dto.setStatut(determinerStatut(soutenance));
        
        return dto;
    }
    
    private String determinerStatut(Soutenance soutenance) {
        LocalDate today = LocalDate.now();
        
        if (soutenance.getNote() != null) {
            return "Terminée";
        } else if (soutenance.getDate().isBefore(today)) {
            return "En retard";
        } else if (soutenance.getDate().equals(today)) {
            return "En cours";
        } else {
            return "Planifiée";
        }
    }
}