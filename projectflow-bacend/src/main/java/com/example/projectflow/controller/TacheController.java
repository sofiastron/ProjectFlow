package com.example.projectflow.controller;

import com.example.projectflow.DTOs.TacheDTO;
import com.example.projectflow.service.TacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/taches")
@CrossOrigin(origins = "*")
public class TacheController {

    @Autowired
    private TacheService tacheService;

    // GET /api/taches/kanban/{sujetId}
    @GetMapping("/kanban/{sujetId}")
    public ResponseEntity<Map<String, List<TacheDTO>>> getKanban(
            @PathVariable Integer sujetId) {

        Map<String, List<TacheDTO>> kanban = tacheService.getTachesKanban(sujetId);
        return ResponseEntity.ok(kanban);
    }

    // GET /api/taches
    @GetMapping
    public ResponseEntity<List<TacheDTO>> getAllTaches() {
        List<TacheDTO> taches = tacheService.getAllTaches();
        return ResponseEntity.ok(taches);
    }

    // GET /api/taches/{id}
    @GetMapping("/{id}")
    public ResponseEntity<TacheDTO> getTacheById(@PathVariable Integer id) {
        TacheDTO tache = tacheService.getTacheById(id);
        return ResponseEntity.ok(tache);
    }

    // POST /api/taches
    @PostMapping
    public ResponseEntity<TacheDTO> createTache(@RequestBody TacheDTO tacheDTO) {
        TacheDTO newTache = tacheService.createTache(tacheDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newTache);
    }

    // PUT /api/taches/{id}
    @PutMapping("/{id}")
    public ResponseEntity<TacheDTO> updateTache(
            @PathVariable Integer id,
            @RequestBody TacheDTO tacheDTO) {

        TacheDTO updatedTache = tacheService.updateTache(id, tacheDTO);
        return ResponseEntity.ok(updatedTache);
    }

    // PUT /api/taches/{id}/etat
    @PutMapping("/{id}/etat")
    public ResponseEntity<TacheDTO> changerEtat(
            @PathVariable Integer id,
            @RequestBody Map<String, String> body) {

        String nouvelEtat = body.get("etat");
        TacheDTO tache = tacheService.changerEtat(id, nouvelEtat);
        return ResponseEntity.ok(tache);
    }

    // PUT /api/taches/{id}/valider
    @PutMapping("/{id}/valider")
    public ResponseEntity<TacheDTO> validerTache(@PathVariable Integer id) {
        TacheDTO tache = tacheService.validerTache(id);
        return ResponseEntity.ok(tache);
    }

    // DELETE /api/taches/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTache(@PathVariable Integer id) {
        tacheService.deleteTache(id);
        return ResponseEntity.noContent().build();
    }
}