package com.example.projectflow.repository;

import com.example.projectflow.entity.etudiant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EtudiantRepository extends JpaRepository<etudiant, Long> {
    Optional<etudiant> findByUserEmail(String email);
}
