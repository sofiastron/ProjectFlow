package com.example.projectflow.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "salle")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Salle {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true, length = 50)
    private String nom;
    
    @Column(length = 100)
    private String batiment;
    
    private Integer capacite;
    
    @Column(columnDefinition = "TEXT")
    private String equipements; // projecteur, tableau interactif, etc.
    
    @Column(nullable = false)
    private Boolean disponible = true;
    
    @Transient
    public String getNomComplet() {
        return batiment != null ? batiment + " - " + nom : nom;
    }
}
