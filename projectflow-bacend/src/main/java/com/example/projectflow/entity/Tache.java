package com.example.projectflow.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "tache")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tache {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 200)
    private String titre;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "dureeJours")
    private Integer dureeJours;
    
    @Column(name = "dateDebut")
    private LocalDate dateDebut;
    
    @Column(name = "dateFin")
    private LocalDate dateFin;
    
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('En cours','Terminé','En retard') DEFAULT 'En cours'")
    private Etat etat = Etat.EN_COURS;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sujet_id", nullable = false)
    private Sujet sujet;
    
    public enum Etat {
        EN_COURS("En cours"),
        TERMINE("Terminé"),
        EN_RETARD("En retard");
        
        private final String label;
        
        Etat(String label) {
            this.label = label;
        }
        
        public String getLabel() {
            return label;
        }
    }
}