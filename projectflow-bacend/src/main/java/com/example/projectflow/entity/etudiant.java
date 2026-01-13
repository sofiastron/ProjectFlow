package com.example.projectflow.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "etudiant") // correspond à la table SQL
public class etudiant {

    @Id
    private Long id; 

    @OneToOne
    @MapsId // utilise le même id que User
    @JoinColumn(name = "id")
    private User user;

    private String filiere;

    @Column(unique = true) // IMPORT AJOUTÉ pour éviter l'erreur
    private String cin;

    @ManyToOne
    @JoinColumn(name = "professeur_id")
    private Professeur professeur;

    // ----- Getters & Setters -----
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getFiliere() {
        return filiere;
    }

    public void setFiliere(String filiere) {
        this.filiere = filiere;
    }

    public String getCin() {
        return cin;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }

    public Professeur getProfesseur() {
        return professeur;
    }

    public void setProfesseur(Professeur professeur) {
        this.professeur = professeur;
    }
}
