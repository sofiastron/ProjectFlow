package com.example.projectflow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SoutenanceDTO {
    
    private Long id;
    private LocalDate date;
    private LocalTime heure;
    private String salle;
    private Float note;
    private String rapportFinal;
    
    // Étudiant
    private Long etudiantId;
    private String etudiantNom;
    private String etudiantEmail;
    private String filiere;
    
    // Encadrant
    private Long encadrantId;
    private String encadrantNom;
    
    // Jury
    private JuryDTO jury;
    
    // Statut
    private String statut; // "Planifiée", "En cours", "Terminée", "Annulée"
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class JuryDTO {
        private Long presidentId;
        private String presidentNom;
        
        private Long rapporteurId;
        private String rapporteurNom;
        
        private Long examinateurId;
        private String examinateurNom;
    }
    
    // Request pour créer/modifier une soutenance
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SoutenanceRequest {
        private Long etudiantId;
        private LocalDate date;
        private LocalTime heure;
        private String salle;
        
        // Jury
        private Long presidentId;
        private Long rapporteurId;
        private Long examinateurId;
        
        private String rapportFinal;
    }
    
    // Réponse de planification
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PlanificationResponse {
        private Boolean succes;
        private String message;
        private SoutenanceDTO soutenance;
        private java.util.List<String> conflits;
    }
    
    // DTO pour calendrier
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CalendrierSoutenanceDTO {
        private LocalDate date;
        private java.util.List<SoutenanceDTO> soutenances;
        private Integer nombreSoutenances;
    }
}