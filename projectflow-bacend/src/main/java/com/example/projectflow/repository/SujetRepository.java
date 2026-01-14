package com.example.projectflow.repository;

import java.util.List; 

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.projectflow.entity.Sujet;

@Repository
public interface SujetRepository extends JpaRepository<Sujet, Long> {

    
    List<Sujet> findByEtudiantId(Long etudiantId);

    
    int countByEtudiantIdAndProgression(Long etudiantId, int progression);
    int countByEtudiantIdAndProgressionLessThan(Long etudiantId, int progression);
}
