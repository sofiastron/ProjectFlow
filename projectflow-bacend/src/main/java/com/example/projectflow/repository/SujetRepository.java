package com.example.projectflow.repository;

import com.example.projectflow.model.Sujet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SujetRepository extends JpaRepository<Sujet, Integer> {
    List<Sujet> findByProfesseurId(Integer professeurId);
    Sujet findByEtudiantId(Integer etudiantId);
}