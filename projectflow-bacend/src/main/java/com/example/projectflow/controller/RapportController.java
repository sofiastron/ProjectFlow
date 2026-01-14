package com.example.projectflow.controller;

import com.example.projectflow.dto.ApiResponse;
import com.example.projectflow.dto.RapportDTO;
import com.example.projectflow.entity.Rapport;
import com.example.projectflow.service.RapportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rapports")
@CrossOrigin("*")
public class RapportController {

    @Autowired
    private RapportService rapportService;

    /**
     * GET /rapports - Affiche la liste complète pour l'Admin
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<RapportDTO>>> getAllRapports() {
        try {
            List<RapportDTO> rapportsDTO = rapportService.getAllRapportsDTO();
            return ResponseEntity.ok(
                ApiResponse.success(rapportsDTO, "Rapports récupérés avec succès")
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Erreur lors de la récupération des rapports", e.getMessage()));
        }
    }


    /**
     * GET /rapports/statistiques - Statistiques des rapports
     */
    @GetMapping("/statistiques")
    public ResponseEntity<ApiResponse<Map<String, Long>>> getStatistiques() {
        try {
            Map<String, Long> stats = new HashMap<>();
            
            List<RapportDTO> rapports = rapportService.getAllRapportsDTO();
            
            long total = rapports.size();
            long valides = rapports.stream()
                .filter(r -> "Validé".equals(r.getStatut()))
                .count();
            long enCours = rapports.stream()
                .filter(r -> "En cours".equals(r.getStatut()))
                .count();
            long aCorriger = rapports.stream()
                .filter(r -> "À corriger".equals(r.getStatut()))
                .count();
            
            stats.put("total", total);
            stats.put("valides", valides);
            stats.put("enCours", enCours);
            stats.put("aCorriger", aCorriger);
            
            return ResponseEntity.ok(
                ApiResponse.success(stats, "Statistiques des rapports")
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Erreur lors du calcul des statistiques", e.getMessage()));
        }
    }

    /**
     * PATCH /rapports/{id}/valider - Valider le rapport (Passe le statut en "Validé")
     */
    @PatchMapping("/{id}/valider")
    public ResponseEntity<ApiResponse<Void>> validerRapport(@PathVariable Long id) {
        try {
            rapportService.validerParEncadrant(id);
            return ResponseEntity.ok(
                ApiResponse.success(null, "Rapport validé avec succès")
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Erreur lors de la validation", e.getMessage()));
        }
    }

    /**
     * PATCH /rapports/{id}/corriger - Demander des corrections (Passe le statut en "À corriger")
     */
    @PatchMapping("/{id}/corriger")
    public ResponseEntity<ApiResponse<Void>> demanderCorrection(@PathVariable Long id) {
        try {
            rapportService.demanderCorrection(id);
            return ResponseEntity.ok(
                ApiResponse.success(null, "Correction demandée avec succès")
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Erreur lors de la demande de correction", e.getMessage()));
        }
    }

    /**
     * POST /rapports - Créer un nouveau rapport
     */
    @PostMapping
    public ResponseEntity<ApiResponse<Rapport>> createRapport(@RequestBody Rapport rapport) {
        try {
            Rapport nouveauRapport = rapportService.saveRapport(rapport);
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(nouveauRapport, "Rapport créé avec succès"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Erreur lors de la création du rapport", e.getMessage()));
        }
    }
}