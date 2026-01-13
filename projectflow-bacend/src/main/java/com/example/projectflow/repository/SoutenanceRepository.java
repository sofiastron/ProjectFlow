package com.example.projectflow.repository;

import com.example.projectflow.model.Soutenance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Date;

@Repository
public interface SoutenanceRepository extends JpaRepository<Soutenance, Integer> {
    List<Soutenance> findByProfesseurId(Integer professeurId);
    List<Soutenance> findByDate(Date date);
    Soutenance findByEtudiantId(Integer etudiantId);
}