package com.example.projectflow.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "professeur")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@PrimaryKeyJoinColumn(name = "id")
public class Professeur extends User {
    
    @Column(length = 100)
    private String specialite;
    
    @OneToMany(mappedBy = "professeur", fetch = FetchType.LAZY)
    private List<Etudiant> etudiants;
    
    @OneToMany(mappedBy = "professeur", fetch = FetchType.LAZY)
    private List<Sujet> sujets;
    
    // Champ utile pour la gestion de la charge
    @Transient
    private Integer capaciteMax = 5; // Paramétrable
    
    @Transient
    public Integer getNombreEtudiants() {
        return etudiants != null ? etudiants.size() : 0;
    }
    
    @Transient
    public Boolean estDisponible() {
        return getNombreEtudiants() < capaciteMax;
    }
    
    @Transient
    public Double getTauxCharge() {
        return (getNombreEtudiants().doubleValue() / capaciteMax) * 100;
    }
}