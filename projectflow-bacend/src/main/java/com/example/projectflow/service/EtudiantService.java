package com.example.projectflow.service;

import com.example.projectflow.model.Etudiant;
import com.example.projectflow.model.Sujet;
import com.example.projectflow.DTOs.EtudiantWithSujetDTO;
import com.example.projectflow.DTOs.SujetSimpleDTO;
import com.example.projectflow.repository.EtudiantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EtudiantService {

    @Autowired
    private EtudiantRepository etudiantRepository;

    // Récupérer tous les étudiants affectés à un professeur
    public List<EtudiantWithSujetDTO> getEtudiantsAffectes(Integer profId) {
        List<Etudiant> etudiants = etudiantRepository.findByProfesseurId(profId);

        return etudiants.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Récupérer tous les étudiants
    public List<Etudiant> getAllEtudiants() {
        return etudiantRepository.findAll();
    }

    // Récupérer un étudiant par ID
    public Etudiant getEtudiantById(Integer id) {
        return etudiantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Étudiant non trouvé avec l'ID: " + id));
    }

    // Créer un étudiant
    public Etudiant createEtudiant(Etudiant etudiant) {
        return etudiantRepository.save(etudiant);
    }

    // Mettre à jour un étudiant
    public Etudiant updateEtudiant(Integer id, Etudiant etudiantDetails) {
        Etudiant etudiant = getEtudiantById(id);

        etudiant.setNom(etudiantDetails.getNom());
        etudiant.setEmail(etudiantDetails.getEmail());
        etudiant.setFiliere(etudiantDetails.getFiliere());
        etudiant.setCin(etudiantDetails.getCin());

        if (etudiantDetails.getPassword() != null && !etudiantDetails.getPassword().isEmpty()) {
            etudiant.setPassword(etudiantDetails.getPassword());
        }

        return etudiantRepository.save(etudiant);
    }

    // Supprimer un étudiant
    public void deleteEtudiant(Integer id) {
        Etudiant etudiant = getEtudiantById(id);
        etudiantRepository.delete(etudiant);
    }

    // Conversion vers DTO
    private EtudiantWithSujetDTO convertToDTO(Etudiant etudiant) {
        EtudiantWithSujetDTO dto = new EtudiantWithSujetDTO();
        dto.setId(etudiant.getId());
        dto.setNom(etudiant.getNom());
        dto.setEmail(etudiant.getEmail());
        dto.setFiliere(etudiant.getFiliere());
        dto.setCin(etudiant.getCin());

        if (etudiant.getSujet() != null) {
            Sujet sujet = etudiant.getSujet();
            SujetSimpleDTO sujetDTO = new SujetSimpleDTO();
            sujetDTO.setId(sujet.getId());
            sujetDTO.setTitre(sujet.getTitre());
            sujetDTO.setDescription(sujet.getDescription());
            dto.setSujet(sujetDTO);
        }

        return dto;
    }
}