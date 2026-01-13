package com.example.projectflow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AffectationDTO {
    
    private Long etudiantId;
    private String etudiantNom;
    private String etudiantEmail;
    private String etudiantCin;
    private String filiere;
    
    private Long encadrantId;
    private String encadrantNom;
    private String encadrantEmail;
    private String encadrantSpecialite;
    
    private LocalDateTime dateAffectation;
    private String modeAffectation; // "Manuel" ou "Automatique"
    private String statut; // "Actif", "En attente", "Terminé"
    
    // Informations supplémentaires
    private Long sujetId;
    private String sujetTitre;
    
    // Pour les affectations manuelles
    @lombok.Data
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class AffectationManuelleRequest {
        private Long etudiantId;
        private Long encadrantId;
        private String commentaire;
    }
    
    // Pour les affectations automatiques
    @lombok.Data
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class AffectationAutomatiqueRequest {
        private String filiere; // Si null, tous les étudiants sans encadrant
        private Boolean repartitionEquitable = true;
        private Integer capaciteMaxParEncadrant = 5;
    }
    
    // Résultat d'affectation automatique
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResultatAffectationAuto {
        private Integer nombreAffectations;
        private Integer nombreEchecs;
        private String message;
        private java.util.List<String> detailsEchecs;
    }
}