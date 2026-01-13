package com.example.projectflow.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "soutenance")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Soutenance {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private LocalDate date;
    
    @Column(nullable = false)
    private LocalTime heure;
    
    @Column(length = 50)
    private String salle;
    
    @Column(columnDefinition = "TEXT")
    private String jury; // Format JSON stocké en texte (pour compatibilité avec DB existante)
    
    private Float note;
    
    @Column(name = "rapportFinal", length = 255)
    private String rapportFinal;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "etudiant_id", nullable = false)
    private Etudiant etudiant;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "professeur_id", nullable = false)
    private Professeur professeur;
    
    // Relations pour jury structuré (si on veut améliorer la structure)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "president_id")
    private Professeur president;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rapporteur_id")
    private Professeur rapporteur;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "examinateur_id")
    private Professeur examinateur;
    
    @Transient
    public Boolean estValidee() {
        return note != null && note >= 10;
    }
}
