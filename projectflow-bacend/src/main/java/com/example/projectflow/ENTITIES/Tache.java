package com.example.projectflow.ENTITIES;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "tache")
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

    // Constructeurs
    public Tache() {}

    public Tache(Integer id, String titre, String description, Integer dureeJours,
                 LocalDate dateDebut, LocalDate dateFin, Etat etat, Sujet sujet) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.dureeJours = dureeJours;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.etat = etat;
        this.sujet = sujet;
    }

    // Getters et Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getDureeJours() {
        return dureeJours;
    }

    public void setDureeJours(Integer dureeJours) {
        this.dureeJours = dureeJours;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public Etat getEtat() {
        return etat;
    }

    public void setEtat(Etat etat) {
        this.etat = etat;
    }

    public Sujet getSujet() {
        return sujet;
    }

    public void setSujet(Sujet sujet) {
        this.sujet = sujet;
    }

    public enum Etat {
        EN_COURS("En cours"),
        TERMINE("Terminé"),
        EN_RETARD("En retard");

        private final String value;

        Etat(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
