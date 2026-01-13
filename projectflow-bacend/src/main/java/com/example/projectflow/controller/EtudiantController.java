package com.example.projectflow.controller;

import com.example.projectflow.model.Etudiant;
import com.example.projectflow.DTOs.EtudiantWithSujetDTO;
import com.example.projectflow.service.EtudiantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/etudiants")
@CrossOrigin(origins = "*")
public class EtudiantController {

    @Autowired
    private EtudiantService etudiantService;

    // GET /api/etudiants/professeur/{profId}
    @GetMapping("/professeur/{profId}")
    public ResponseEntity<List<EtudiantWithSujetDTO>> getEtudiantsAffectes(
            @PathVariable Integer profId) {

        List<EtudiantWithSujetDTO> etudiants =
                etudiantService.getEtudiantsAffectes(profId);

        return ResponseEntity.ok(etudiants);
    }

    // GET /api/etudiants
    @GetMapping
    public ResponseEntity<List<Etudiant>> getAllEtudiants() {
        List<Etudiant> etudiants = etudiantService.getAllEtudiants();
        return ResponseEntity.ok(etudiants);
    }

    // GET /api/etudiants/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Etudiant> getEtudiantById(@PathVariable Integer id) {
        Etudiant etudiant = etudiantService.getEtudiantById(id);
        return ResponseEntity.ok(etudiant);
    }

    // POST /api/etudiants
    @PostMapping
    public ResponseEntity<Etudiant> createEtudiant(@RequestBody Etudiant etudiant) {
        Etudiant newEtudiant = etudiantService.createEtudiant(etudiant);
        return ResponseEntity.status(HttpStatus.CREATED).body(newEtudiant);
    }

    // PUT /api/etudiants/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Etudiant> updateEtudiant(
            @PathVariable Integer id,
            @RequestBody Etudiant etudiant) {

        Etudiant updatedEtudiant = etudiantService.updateEtudiant(id, etudiant);
        return ResponseEntity.ok(updatedEtudiant);
    }

    // DELETE /api/etudiants/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEtudiant(@PathVariable Integer id) {
        etudiantService.deleteEtudiant(id);
        return ResponseEntity.noContent().build();
    }
}