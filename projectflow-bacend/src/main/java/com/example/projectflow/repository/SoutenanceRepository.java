package com.example.projectflow.repository;

import com.example.projectflow.entity.soutenance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SoutenanceRepository extends JpaRepository<soutenance, Long> {

    List<soutenance> findByEtudiantId(Long etudiantId);
}
