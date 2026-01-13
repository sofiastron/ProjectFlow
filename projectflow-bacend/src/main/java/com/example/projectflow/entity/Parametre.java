package com.example.projectflow.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "parametre")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Parametre {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true, length = 100)
    private String cle;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String valeur;
    
    @Column(length = 255)
    private String description;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeParametre type = TypeParametre.GENERAL;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    public enum TypeParametre {
        GENERAL,
        AFFECTATION,
        SOUTENANCE,
        NOTIFICATION,
        SYSTEME
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    // Paramètres prédéfinis
    public static final String CAPACITE_MAX_ENCADRANT = "capacite_max_encadrant";
    public static final String DUREE_SOUTENANCE_MIN = "duree_soutenance_minutes";
    public static final String NOTIFICATION_AFFECTATION = "notification_affectation_enabled";
    public static final String NOTIFICATION_SOUTENANCE = "notification_soutenance_enabled";
    public static final String DELAI_MIN_SOUTENANCE_JOURS = "delai_min_soutenance_jours";
}
