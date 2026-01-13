package com.example.projectflow.REPOSITORIES;


import com.example.projectflow.ENTITIES.Sujet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SujetRepository extends JpaRepository<Sujet, Integer> {
    List<Sujet> findByProfesseurId(Integer id);
}