package com.example.projectflow.REPOSITORIES;

import com.example.projectflow.ENTITIES.Professeur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfesseurRepository extends JpaRepository<Professeur, Integer> {
}