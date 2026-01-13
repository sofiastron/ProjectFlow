package com.example.projectflow.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Entity
@Table(name = "creneau")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Creneau {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private LocalTime heureDebut;
    
    @Column(nullable = false)
    private LocalTime heureFin;
    
    @Column(length = 100)
    private String description; // Matin, Après-midi, etc.
    
    @Column(nullable = false)
    private Boolean disponible = true;
    
    @Transient
    public String getPlageHoraire() {
        return heureDebut + " - " + heureFin;
    }
    
    @Transient
    public Integer getDureeMinutes() {
        return (heureFin.getHour() - heureDebut.getHour()) * 60 
             + (heureFin.getMinute() - heureDebut.getMinute());
    }
}
