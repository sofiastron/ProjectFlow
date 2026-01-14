package com.example.projectflow.ENTITIES;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "professeur")
public class Professeur {
    @Id
    private Integer id;

    private String specialite;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private User user;

    @OneToMany(mappedBy = "professeur")
    private List<Etudiant> etudiants;

    @OneToMany(mappedBy = "professeur")
    private List<Sujet> sujets;

    // Constructeurs
    public Professeur() {}

    public Professeur(Integer id, String specialite, User user,
                      List<Etudiant> etudiants, List<Sujet> sujets) {
        this.id = id;
        this.specialite = specialite;
        this.user = user;
        this.etudiants = etudiants;
        this.sujets = sujets;
    }

    // Getters et Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSpecialite() {
        return specialite;
    }

    public void setSpecialite(String specialite) {
        this.specialite = specialite;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Etudiant> getEtudiants() {
        return etudiants;
    }

    public void setEtudiants(List<Etudiant> etudiants) {
        this.etudiants = etudiants;
    }

    public List<Sujet> getSujets() {
        return sujets;
    }

    public void setSujets(List<Sujet> sujets) {
        this.sujets = sujets;
    }
}