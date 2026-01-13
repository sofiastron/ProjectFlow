package com.example.projectflow.repository;

import com.example.projectflow.model.Etudiant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EtudiantRepository extends JpaRepository<Etudiant, Integer> {

    // Trouver tous les étudiants affectés à un professeur
    @Query("SELECT e FROM Etudiant e WHERE e.sujet.professeur.id = :profId")
    List<Etudiant> findByProfesseurId(@Param("profId") Integer profId);

    // Trouver un étudiant par CIN
    Etudiant findByCin(String cin);

    // Trouver les étudiants par filière
    List<Etudiant> findByFiliere(String filiere);
}