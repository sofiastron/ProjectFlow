package com.example.projectflow.repository;

import com.example.projectflow.model.Professeur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProfesseurRepository extends JpaRepository<Professeur, Integer> {
    List<Professeur> findBySpecialite(String specialite);
}