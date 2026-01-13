package com.example.projectflow.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Table(name = "etudiants")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Etudiant extends User {

    @Column(nullable = false)
    private String filiere;

    @Column(nullable = false, unique = true)
    private String cin;

    @OneToOne(mappedBy = "etudiant")
    private Sujet sujet;

    @OneToMany(mappedBy = "etudiant", cascade = CascadeType.ALL)
    private List<Message> messages;

    // Constructeur
    public Etudiant(Integer id, String nom, String email, String password, String filiere, String cin) {
        super(id, nom, email, password, Role.ETUDIANT);
        this.filiere = filiere;
        this.cin = cin;
    }
}