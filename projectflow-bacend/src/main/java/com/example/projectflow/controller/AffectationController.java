package com.example.projectflow.controller;

import com.example.projectflow.dto.AffectationDTO;
import com.example.projectflow.dto.ApiResponse;
import com.example.projectflow.dto.EncadrantChargeDTO;
import com.example.projectflow.service.AffectationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/affectations")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class AffectationController {
    
    private final AffectationService affectationService;
    
    /**
     * GET /affectations - Obtenir toutes les affectations
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<AffectationDTO>>> getAllAffectations() {
        try {
            List<AffectationDTO> affectations = affectationService.getAllAffectations();
            return ResponseEntity.ok(
                ApiResponse.success(affectations, "Affectations récupérées avec succès")
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Erreur lors de la récupération des affectations", e.getMessage()));
        }
    }
    
    /**
     * GET /affectations/filiere/{filiere} - Affectations par filière
     */
    @GetMapping("/filiere/{filiere}")
    public ResponseEntity<ApiResponse<List<AffectationDTO>>> getAffectationsByFiliere(
            @PathVariable String filiere) {
        try {
            List<AffectationDTO> affectations = affectationService.getAffectationsByFiliere(filiere);
            return ResponseEntity.ok(
                ApiResponse.success(affectations, "Affectations de la filière " + filiere)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Erreur", e.getMessage()));
        }
    }
    
    /**
     * POST /affectations/manuelle - Affectation manuelle
     */
    @PostMapping("/manuelle")
    public ResponseEntity<ApiResponse<AffectationDTO>> affecterManuellement(
            @RequestBody AffectationDTO.AffectationManuelleRequest request) {
        try {
            AffectationDTO result = affectationService.affecterManuellement(
                request.getEtudiantId(), 
                request.getEncadrantId()
            );
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(result, "Affectation manuelle réussie"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Erreur lors de l'affectation", e.getMessage()));
        }
    }
    
    /**
     * POST /affectations/automatique - Affectation automatique
     */
    @PostMapping("/automatique")
    public ResponseEntity<ApiResponse<AffectationDTO.ResultatAffectationAuto>> affecterAutomatiquement(
            @RequestBody AffectationDTO.AffectationAutomatiqueRequest request) {
        try {
            AffectationDTO.ResultatAffectationAuto result = 
                affectationService.affecterAutomatiquement(request);
            
            return ResponseEntity.ok(
                ApiResponse.success(result, "Affectation automatique terminée")
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Erreur lors de l'affectation automatique", e.getMessage()));
        }
    }
    
    /**
     * GET /affectations/encadrants/charge - Charge des encadrants
     */
    @GetMapping("/encadrants/charge")
    public ResponseEntity<ApiResponse<List<EncadrantChargeDTO>>> getChargeEncadrants() {
        try {
            List<EncadrantChargeDTO> charges = affectationService.getChargeEncadrants();
            return ResponseEntity.ok(
                ApiResponse.success(charges, "Charge des encadrants récupérée")
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Erreur", e.getMessage()));
        }
    }
    
    /**
     * GET /affectations/encadrants/filiere/{filiere} - Encadrants par filière
     */
    @GetMapping("/encadrants/filiere/{filiere}")
    public ResponseEntity<ApiResponse<List<EncadrantChargeDTO>>> getEncadrantsByFiliere(
            @PathVariable String filiere) {
        try {
            List<EncadrantChargeDTO> encadrants = affectationService.getEncadrantsByFiliere(filiere);
            return ResponseEntity.ok(
                ApiResponse.success(encadrants, "Encadrants de la filière " + filiere)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Erreur", e.getMessage()));
        }
    }
    
    /**
     * DELETE /affectations/{etudiantId} - Désaffecter un étudiant
     */
    @DeleteMapping("/{etudiantId}")
    public ResponseEntity<ApiResponse<Void>> desaffecter(@PathVariable Long etudiantId) {
        try {
            affectationService.desaffecter(etudiantId);
            return ResponseEntity.ok(
                ApiResponse.success(null, "Étudiant désaffecté avec succès")
            );
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Erreur lors de la désaffectation", e.getMessage()));
        }
    }
}