package com.example.projectflow.ENTITIES;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "tache")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tache {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

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
    private Etat etat;

    @ManyToOne
    @JoinColumn(name = "sujet_id", nullable = false)
    private Sujet sujet;

    public Etat getEtat() {
        return this.etat;
    }

    public enum Etat {
        EN_COURS, TERMINE, EN_RETARD
    }
}