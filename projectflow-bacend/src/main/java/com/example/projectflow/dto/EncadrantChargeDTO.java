package com.example.projectflow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EncadrantChargeDTO {
    
    private Long id;
    private String nom;
    private String email;
    private String specialite;
    private Integer nombreEtudiants;
    private Integer capaciteMax;
    private Double tauxCharge; // En pourcentage
    private String statut; // "Disponible", "Charge moyenne", "Saturé"
    private String couleurStatut; // Pour le badge UI
    
    public EncadrantChargeDTO(Long id, String nom, String email, String specialite, 
                              Integer nombreEtudiants, Integer capaciteMax) {
        this.id = id;
        this.nom = nom;
        this.email = email;
        this.specialite = specialite;
        this.nombreEtudiants = nombreEtudiants;
        this.capaciteMax = capaciteMax;
        this.tauxCharge = calculateTauxCharge();
        this.statut = calculateStatut();
        this.couleurStatut = calculateCouleurStatut();
    }
    
    private Double calculateTauxCharge() {
        if (capaciteMax == null || capaciteMax == 0) return 0.0;
        return (nombreEtudiants.doubleValue() / capaciteMax) * 100;
    }
    
    private String calculateStatut() {
        if (tauxCharge >= 100) return "Saturé";
        if (tauxCharge >= 60) return "Charge moyenne";
        return "Disponible";
    }
    
    private String calculateCouleurStatut() {
        if (tauxCharge >= 100) return "#FF6B6B"; // Rouge
        if (tauxCharge >= 60) return "#FFD275"; // Jaune
        return "#D7F6E4"; // Vert pastel
    }
    
    public Boolean estDisponible() {
        return nombreEtudiants < capaciteMax;
    }
}
