package com.example.projectflow.repository;

import com.example.projectflow.model.CahierCharges;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CahierChargesRepository extends JpaRepository<CahierCharges, Long> {
}