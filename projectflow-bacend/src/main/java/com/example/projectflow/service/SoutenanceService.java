package com.example.projectflow.service;

import com.example.projectflow.entity.soutenance;
import com.example.projectflow.repository.SoutenanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SoutenanceService {

    @Autowired
    private SoutenanceRepository soutenanceRepo;

    public List<soutenance> getSoutenancesByEtudiant(Long etudiantId) {
        return soutenanceRepo.findByEtudiantId(etudiantId);
    }
}
