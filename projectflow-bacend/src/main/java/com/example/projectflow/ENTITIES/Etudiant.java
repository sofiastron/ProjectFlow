package com.example.projectflow.ENTITIES;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "etudiant")
public class Etudiant {
    @Id
    private Integer id;

    private String filiere;
    private String cin;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "professeur_id")
    private Professeur professeur;

    @OneToMany(mappedBy = "etudiant")
    private List<Sujet> sujets;

    // Constructeurs
    public Etudiant() {}

    public Etudiant(Integer id, String filiere, String cin, User user,
                    Professeur professeur, List<Sujet> sujets) {
        this.id = id;
        this.filiere = filiere;
        this.cin = cin;
        this.user = user;
        this.professeur = professeur;
        this.sujets = sujets;
    }

    // Getters et Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Professeur getProfesseur() {
        return professeur;
    }

    public void setProfesseur(Professeur professeur) {
        this.professeur = professeur;
    }

    public List<Sujet> getSujets() {
        return sujets;
    }

    public void setSujets(List<Sujet> sujets) {
        this.sujets = sujets;
    }
}