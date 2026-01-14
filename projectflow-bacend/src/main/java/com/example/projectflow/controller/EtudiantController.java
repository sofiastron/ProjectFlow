package com.example.projectflow.controller;

import com.example.projectflow.dto.ApiResponse;
import com.example.projectflow.dto.EtudiantDTO;
import com.example.projectflow.entity.Etudiant;
import com.example.projectflow.service.EtudiantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/etudiants")
@CrossOrigin("*")
public class EtudiantController {

    @Autowired
    private EtudiantService etudiantService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<EtudiantDTO>>> getAllEtudiants() {
        List<EtudiantDTO> dtos = etudiantService.getAllEtudiants()
            .stream()
            .map(EtudiantDTO::new)
            .toList();

        return ResponseEntity.ok(
            ApiResponse.success(dtos, "Étudiants récupérés avec succès")
        );
    }


    @GetMapping("/filiere/{filiere}")
    public ResponseEntity<ApiResponse<List<Etudiant>>> getByFiliere(@PathVariable String filiere) {
        List<Etudiant> etudiants = etudiantService.getEtudiantsParFiliere(filiere);
        return ResponseEntity.ok(
            ApiResponse.success(etudiants, "Étudiants de la filière " + filiere)
        );
    }
    
    @GetMapping("/statistiques")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getStatistiques() {
        List<Etudiant> allEtudiants = etudiantService.getAllEtudiants();
        long total = allEtudiants.size();
        long affectes = allEtudiants.stream().filter(e -> e.getProfesseur() != null).count();
        long nonAffectes = total - affectes;

        Map<String, Object> stats = new HashMap<>();
        stats.put("total", total);
        stats.put("affectes", affectes);
        stats.put("nonAffectes", nonAffectes);

        return ResponseEntity.ok(ApiResponse.success(stats, "Statistiques des étudiants"));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Etudiant>> createEtudiant(@RequestBody Etudiant etudiant) {
        Etudiant saved = etudiantService.saveEtudiant(etudiant);
        return ResponseEntity.ok(ApiResponse.success(saved, "Étudiant ajouté avec succès"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Etudiant>> updateEtudiant(@PathVariable Long id, @RequestBody Etudiant etudiant) {
        Etudiant updated = etudiantService.updateEtudiant(id, etudiant);
        return ResponseEntity.ok(ApiResponse.success(updated, "Étudiant modifié avec succès"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteEtudiant(@PathVariable Long id) {
        etudiantService.deleteEtudiant(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Étudiant supprimé avec succès"));
    }
    


}