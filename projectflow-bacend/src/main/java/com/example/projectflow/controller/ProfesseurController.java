package com.example.projectflow.controller;

import com.example.projectflow.model.Professeur;
import com.example.projectflow.service.ProfesseurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/professeurs")
@CrossOrigin(origins = "*")
public class ProfesseurController {

    @Autowired
    private ProfesseurService professeurService;

    // GET /api/professeurs
    @GetMapping
    public ResponseEntity<List<Professeur>> getAllProfesseurs() {
        List<Professeur> professeurs = professeurService.getAllProfesseurs();
        return ResponseEntity.ok(professeurs);
    }

    // GET /api/professeurs/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Professeur> getProfesseurById(@PathVariable Integer id) {
        Professeur professeur = professeurService.getProfesseurById(id);
        return ResponseEntity.ok(professeur);
    }

    // POST /api/professeurs
    @PostMapping
    public ResponseEntity<Professeur> createProfesseur(@RequestBody Professeur professeur) {
        Professeur newProfesseur = professeurService.createProfesseur(professeur);
        return ResponseEntity.status(HttpStatus.CREATED).body(newProfesseur);
    }

    // PUT /api/professeurs/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Professeur> updateProfesseur(
            @PathVariable Integer id,
            @RequestBody Professeur professeur) {

        Professeur updatedProfesseur = professeurService.updateProfesseur(id, professeur);
        return ResponseEntity.ok(updatedProfesseur);
    }

    // DELETE /api/professeurs/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfesseur(@PathVariable Integer id) {
        professeurService.deleteProfesseur(id);
        return ResponseEntity.noContent().build();
    }
}