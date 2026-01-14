package com.example.projectflow.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.projectflow.entity.Sujet;
import com.example.projectflow.repository.SujetRepository;

@Service
public class SujetService {

    @Autowired
    private SujetRepository sujetRepo; 

   
    public List<Sujet> getProjetsByEtudiant(Long etudiantId) {
        return sujetRepo.findByEtudiantId(etudiantId);
    }

    public Optional<Sujet> getProjetDetails(Long sujetId) {
        return sujetRepo.findById(sujetId);
    }

    public List<Sujet> getSujetsByEtudiant(Long etudiantId) {
        return sujetRepo.findByEtudiantId(etudiantId); 
    }
}
