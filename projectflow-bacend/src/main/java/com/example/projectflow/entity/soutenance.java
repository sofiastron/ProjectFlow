package com.example.projectflow.entity;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "soutenance")
public class soutenance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private LocalTime heure;

    private String salle;

    @Column(columnDefinition = "TEXT")
    private String jury;

    private Float note;

    private String rapportFinal;

    @ManyToOne
    @JoinColumn(name = "etudiant_id", nullable = false)
    private etudiant etudiant;

    @ManyToOne
    @JoinColumn(name = "professeur_id", nullable = false)
    private Professeur professeur;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getHeure() {
        return heure;
    }

    public void setHeure(LocalTime heure) {
        this.heure = heure;
    }

    public String getSalle() {
        return salle;
    }

    public void setSalle(String salle) {
        this.salle = salle;
    }

    public String getJury() {
        return jury;
    }

    public void setJury(String jury) {
        this.jury = jury;
    }

    public Float getNote() {
        return note;
    }

    public void setNote(Float note) {
        if (note != null && (note < 0 || note > 20)) {
            throw new IllegalArgumentException("La note doit être comprise entre 0 et 20.");
        }
        this.note = note;
    }

    public String getRapportFinal() {
        return rapportFinal;
    }

    public void setRapportFinal(String rapportFinal) {
        this.rapportFinal = rapportFinal;
    }

    public etudiant getEtudiant() {
        return etudiant;
    }

    public void setEtudiant(etudiant etudiant) {
        this.etudiant = etudiant;
    }

    public Professeur getProfesseur() {
        return professeur;
    }

    public void setProfesseur(Professeur professeur) {
        this.professeur = professeur;
    }
}
