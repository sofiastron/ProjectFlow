package com.example.projectflow.ENTITIES;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "professeur")
@Data
@NoArgsConstructor
@AllArgsConstructor
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

    public User getUser() {
        return user;
    }

    public void setSpecialite(String specialite) {
        this.specialite=specialite;
    }

    public String getSpecialite() {
        return this.specialite;
    }

    public Integer getId() {
        return this.id;
    }
}

