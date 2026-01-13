package com.example.projectflow.controller;

import com.example.projectflow.dto.ApiResponse;
import com.example.projectflow.dto.SoutenanceDTO;
import com.example.projectflow.service.SoutenanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/soutenances")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class SoutenanceController {
    
    private final SoutenanceService soutenanceService;
    
    /**
     * GET /soutenances - Obtenir toutes les soutenances
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<SoutenanceDTO>>> getAllSoutenances() {
        try {
            List<SoutenanceDTO> soutenances = soutenanceService.getAllSoutenances();
            return ResponseEntity.ok(
                ApiResponse.success(soutenances, "Soutenances récupérées avec succès")
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Erreur", e.getMessage()));
        }
    }
    
    /**
     * GET /soutenances/a-venir - Soutenances à venir
     */
    @GetMapping("/a-venir")
    public ResponseEntity<ApiResponse<List<SoutenanceDTO>>> getSoutenancesAVenir() {
        try {
            List<SoutenanceDTO> soutenances = soutenanceService.getSoutenancesAVenir();
            return ResponseEntity.ok(
                ApiResponse.success(soutenances, "Soutenances à venir")
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Erreur", e.getMessage()));
        }
    }
    
    /**
     * GET /soutenances/periode - Soutenances par période
     */
    @GetMapping("/periode")
    public ResponseEntity<ApiResponse<List<SoutenanceDTO>>> getSoutenancesByPeriode(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin) {
        try {
            List<SoutenanceDTO> soutenances = soutenanceService.getSoutenancesByPeriode(dateDebut, dateFin);
            return ResponseEntity.ok(
                ApiResponse.success(soutenances, "Soutenances de la période")
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Erreur", e.getMessage()));
        }
    }
    
    /**
     * GET /soutenances/calendrier - Calendrier des soutenances
     */
    @GetMapping("/calendrier")
    public ResponseEntity<ApiResponse<List<SoutenanceDTO.CalendrierSoutenanceDTO>>> getCalendrierSoutenances(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin) {
        try {
            List<SoutenanceDTO.CalendrierSoutenanceDTO> calendrier = 
                soutenanceService.getCalendrierSoutenances(dateDebut, dateFin);
            return ResponseEntity.ok(
                ApiResponse.success(calendrier, "Calendrier des soutenances")
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Erreur", e.getMessage()));
        }
    }
    
    /**
     * GET /soutenances/salle - Soutenances par salle
     */
    @GetMapping("/salle")
    public ResponseEntity<ApiResponse<List<SoutenanceDTO>>> getSoutenancesBySalle(
            @RequestParam String salle,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        try {
            List<SoutenanceDTO> soutenances = soutenanceService.getSoutenancesBySalle(salle, date);
            return ResponseEntity.ok(
                ApiResponse.success(soutenances, "Soutenances de la salle " + salle)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Erreur", e.getMessage()));
        }
    }
    
    /**
     * POST /soutenances - Planifier une soutenance
     */
    @PostMapping
    public ResponseEntity<ApiResponse<SoutenanceDTO.PlanificationResponse>> planifierSoutenance(
            @RequestBody SoutenanceDTO.SoutenanceRequest request) {
        try {
            SoutenanceDTO.PlanificationResponse response = 
                soutenanceService.planifierSoutenance(request);
            
            if (response.getSucces()) {
                return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success(response, "Soutenance planifiée"));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(response.getMessage()));
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Erreur lors de la planification", e.getMessage()));
        }
    }
    
    /**
     * PUT /soutenances/{id} - Modifier une soutenance
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SoutenanceDTO>> modifierSoutenance(
            @PathVariable Long id,
            @RequestBody SoutenanceDTO.SoutenanceRequest request) {
        try {
            SoutenanceDTO soutenance = soutenanceService.modifierSoutenance(id, request);
            return ResponseEntity.ok(
                ApiResponse.success(soutenance, "Soutenance modifiée avec succès")
            );
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Erreur lors de la modification", e.getMessage()));
        }
    }
    
    /**
     * DELETE /soutenances/{id} - Supprimer une soutenance
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> supprimerSoutenance(@PathVariable Long id) {
        try {
            soutenanceService.supprimerSoutenance(id);
            return ResponseEntity.ok(
                ApiResponse.success(null, "Soutenance supprimée avec succès")
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Erreur lors de la suppression", e.getMessage()));
        }
    }
    
    /**
     * PUT /soutenances/{id}/note - Attribuer une note
     */
    @PutMapping("/{id}/note")
    public ResponseEntity<ApiResponse<SoutenanceDTO>> attribuerNote(
            @PathVariable Long id,
            @RequestParam Float note) {
        try {
            SoutenanceDTO soutenance = soutenanceService.attribuerNote(id, note);
            return ResponseEntity.ok(
                ApiResponse.success(soutenance, "Note attribuée avec succès")
            );
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Erreur lors de l'attribution de la note", e.getMessage()));
        }
    }
    
    /**
     * GET /soutenances/jury/disponible - Vérifier disponibilité jury
     */
    @GetMapping("/jury/disponible")
    public ResponseEntity<ApiResponse<Boolean>> isJuryDisponible(
            @RequestParam Long professeurId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime heure) {
        try {
            Boolean disponible = soutenanceService.isJuryDisponible(professeurId, date, heure);
            return ResponseEntity.ok(
                ApiResponse.success(disponible, "Vérification de disponibilité")
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Erreur", e.getMessage()));
        }
    }
}