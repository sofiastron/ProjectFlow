package com.example.projectflow.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalTime;
import java.util.Date;

@Entity
@Table(name = "soutenances")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Soutenance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date date;

    @Column(nullable = false)
    private LocalTime heure;  // ✅ Utiliser LocalTime pour l'heure

    @Column(nullable = false)
    private String salle;

    @Column(length = 500)
    private String jury;

    @Column(nullable = false)
    private Float note;

    @Column(name = "rapport_final")
    private String rapportFinal; // Chemin vers le document

    @ManyToOne
    @JoinColumn(name = "etudiant_id")
    private Etudiant etudiant;

    @ManyToOne
    @JoinColumn(name = "professeur_id")
    private Professeur professeur;
}