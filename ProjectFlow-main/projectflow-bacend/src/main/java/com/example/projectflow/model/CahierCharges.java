package com.example.projectflow.model;

import java.time.LocalDateTime;
import jakarta.persistence.*;
@Entity
@Table(name = "cahier_charges")
public class CahierCharges {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sujet_projet")
    private String sujetProjet;

    @Column(name = "sujet_problematique")
    private String sujetProblematique;

    @Column(name = "besoins_fonctionnels")
    private String besoinsFonctionnels;

    @Column(name = "besoins_non_fonctionnels")
    private String besoinsNonFonctionnels;

    @Column(name = "technologies_proposees")
    private String technologiesProposees;

    @Column(name = "fichier_pdf")
    private String fichierPdf;

    @Column(name = "date_creation")
    private LocalDateTime dateCreation;

    @Column(name = "sujet_id")
    private Long sujetId;

    // Pas de colonnes date_validation et statut dans la table

    // Constructeur
    public CahierCharges() {
        this.dateCreation = LocalDateTime.now();
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSujetProjet() { return sujetProjet; }
    public void setSujetProjet(String sujetProjet) { this.sujetProjet = sujetProjet; }

    public String getSujetProblematique() { return sujetProblematique; }
    public void setSujetProblematique(String sujetProblematique) { this.sujetProblematique = sujetProblematique; }

    public String getBesoinsFonctionnels() { return besoinsFonctionnels; }
    public void setBesoinsFonctionnels(String besoinsFonctionnels) { this.besoinsFonctionnels = besoinsFonctionnels; }

    public String getBesoinsNonFonctionnels() { return besoinsNonFonctionnels; }
    public void setBesoinsNonFonctionnels(String besoinsNonFonctionnels) { this.besoinsNonFonctionnels = besoinsNonFonctionnels; }

    public String getTechnologiesProposees() { return technologiesProposees; }
    public void setTechnologiesProposees(String technologiesProposees) { this.technologiesProposees = technologiesProposees; }

    public String getFichierPdf() { return fichierPdf; }
    public void setFichierPdf(String fichierPdf) { this.fichierPdf = fichierPdf; }

    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }

    public Long getSujetId() { return sujetId; }
    public void setSujetId(Long sujetId) { this.sujetId = sujetId; }

    // Méthodes pour les champs manquants (pour éviter les erreurs de compilation)
    public String getStatut() {
        return "en_attente"; // Valeur par défaut
    }

    public void setStatut(String statut) {
        // Ne rien faire - champ non présent dans la base
    }

    public LocalDateTime getDateValidation() {
        return null; // Champ non présent dans la base
    }

    public void setDateValidation(LocalDateTime dateValidation) {
        // Ne rien faire - champ non présent dans la base
    }
}