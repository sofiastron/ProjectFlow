package com.example.projectflow.ENTITIES;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "etudiant")
@Data
@NoArgsConstructor
@AllArgsConstructor
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

    public  Integer getId() {
        return this.id ;
    }

    public User getUser() {
        return this.user;
    }

    public String getFiliere() {
        return this.filiere;
    }
}