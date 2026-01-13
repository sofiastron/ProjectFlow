package com.example.projectflow.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Table(name = "professeurs")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Professeur extends User {

    @Column(nullable = false)
    private String specialite;

    @OneToMany(mappedBy = "professeur", cascade = CascadeType.ALL)
    private List<Sujet> sujets;

    @OneToMany(mappedBy = "professeur", cascade = CascadeType.ALL)
    private List<Soutenance> soutenances;

    // Constructeur
    public Professeur(Integer id, String nom, String email, String password, String specialite) {
        super(id, nom, email, password, Role.PROFESSEUR);
        this.specialite = specialite;
    }
}