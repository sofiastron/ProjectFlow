package com.example.projectflow.controller;

import com.example.projectflow.dto.ApiResponse;
import com.example.projectflow.dto.EncadrantDTO;
import com.example.projectflow.entity.Professeur;
import com.example.projectflow.service.EncadrantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/encadrants")
@CrossOrigin("*")
public class EncadrantController {

    @Autowired
    private EncadrantService encadrantService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<EncadrantDTO>>> getAllEncadrants() {
        try {
            List<EncadrantDTO> dtos = encadrantService.getAllEncadrants().stream()
                .map(prof -> new EncadrantDTO(
                    prof.getId(),
                    prof.getNom(),
                    prof.getEmail(),
                    prof.getSpecialite(),
                    prof.getNombreEtudiants(),
                    prof.getCapaciteMax(),
                    prof.getTauxCharge()
                )).collect(Collectors.toList());

            return ResponseEntity.ok(
                ApiResponse.success(dtos, "Encadrants récupérés avec succès")
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Erreur", e.getMessage()));
        }
    }

    @GetMapping("/statistiques")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getStatistiques() {
        try {
            Map<String, Object> stats = new HashMap<>();
            
            List<EncadrantDTO> encadrants = encadrantService.getAllEncadrants().stream()
                .map(prof -> new EncadrantDTO(
                    prof.getId(),
                    prof.getNom(),
                    prof.getEmail(),
                    prof.getSpecialite(),
                    prof.getNombreEtudiants(),
                    prof.getCapaciteMax(),
                    prof.getTauxCharge()
                )).collect(Collectors.toList());
            
            stats.put("total", encadrants.size());
            stats.put("disponibles", encadrants.stream()
                .filter(e -> e.getTauxCharge() < 100)
                .count());
            stats.put("satures", encadrants.stream()
                .filter(e -> e.getTauxCharge() >= 100)
                .count());
            
            return ResponseEntity.ok(
                ApiResponse.success(stats, "Statistiques des encadrants")
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Erreur", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<EncadrantDTO>> updateEncadrant(
            @PathVariable Long id,
            @RequestBody EncadrantDTO dto) {

        try {
            Professeur prof = encadrantService.updateEncadrant(id, dto);

            EncadrantDTO responseDto = new EncadrantDTO(
                prof.getId(),
                prof.getNom(),
                prof.getEmail(),
                prof.getSpecialite(),
                prof.getNombreEtudiants(),
                prof.getCapaciteMax(),
                prof.getTauxCharge()
            );

            return ResponseEntity.ok(
                ApiResponse.success(responseDto, "Encadrant modifié avec succès")
            );

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("Erreur", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteEncadrant(@PathVariable Long id) {
        encadrantService.delete(id);
        return ResponseEntity.ok(
            ApiResponse.success(null, "Encadrant supprimé avec succès")
        );
    }


}