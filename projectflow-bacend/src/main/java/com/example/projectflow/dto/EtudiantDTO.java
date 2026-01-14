package com.example.projectflow.dto;

import com.example.projectflow.entity.Etudiant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EtudiantDTO {
    private Long id;
    private String nom;
    private String filiere;
    private boolean affecte;
    private String encadrantNom;

    // Constructeur à partir d'un Etudiant
    public EtudiantDTO(Etudiant e) {
        this.id = e.getId();
        this.nom = e.getNom();
        this.filiere = e.getFiliere();
        this.affecte = e.getProfesseur() != null;
        this.encadrantNom = (e.getProfesseur() != null) ? e.getProfesseur().getNom() : null;
    }
}
