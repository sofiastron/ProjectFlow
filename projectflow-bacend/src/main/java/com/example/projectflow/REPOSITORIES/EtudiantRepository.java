package com.example.projectflow.REPOSITORIES;

import com.example.projectflow.ENTITIES.Etudiant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EtudiantRepository extends JpaRepository<Etudiant, Integer> {
    List<Etudiant> findByProfesseurIdOrderByUserCreatedAtDesc(Integer professeurId);
}