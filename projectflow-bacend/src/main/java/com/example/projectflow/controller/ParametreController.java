package com.example.projectflow.controller;

import com.example.projectflow.dto.ApiResponse;
import com.example.projectflow.entity.Parametre;
import com.example.projectflow.service.ParametreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/parametres")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class ParametreController {
    
    private final ParametreService parametreService;
    
    /**
     * GET /parametres - Obtenir tous les paramètres
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<Parametre>>> getAllParametres() {
        try {
            List<Parametre> parametres = parametreService.getAllParametres();
            return ResponseEntity.ok(
                ApiResponse.success(parametres, "Paramètres récupérés avec succès")
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Erreur", e.getMessage()));
        }
    }
    
    /**
     * GET /parametres/type/{type} - Paramètres par type
     */
    @GetMapping("/type/{type}")
    public ResponseEntity<ApiResponse<List<Parametre>>> getParametresByType(
            @PathVariable Parametre.TypeParametre type) {
        try {
            List<Parametre> parametres = parametreService.getParametresByType(type);
            return ResponseEntity.ok(
                ApiResponse.success(parametres, "Paramètres du type " + type)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Erreur", e.getMessage()));
        }
    }
    
    /**
     * GET /parametres/{cle} - Obtenir un paramètre par clé
     */
    @GetMapping("/{cle}")
    public ResponseEntity<ApiResponse<Parametre>> getParametreByCle(@PathVariable String cle) {
        try {
            Parametre parametre = parametreService.getParametreByCle(cle)
                .orElseThrow(() -> new RuntimeException("Paramètre non trouvé : " + cle));
            return ResponseEntity.ok(
                ApiResponse.success(parametre, "Paramètre récupéré")
            );
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Erreur", e.getMessage()));
        }
    }
    
    /**
     * POST /parametres - Créer ou mettre à jour un paramètre
     */
    @PostMapping
    public ResponseEntity<ApiResponse<Parametre>> saveParametre(@RequestBody Parametre parametre) {
        try {
            Parametre saved = parametreService.saveParametre(parametre);
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(saved, "Paramètre enregistré avec succès"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Erreur lors de l'enregistrement", e.getMessage()));
        }
    }
    
    /**
     * PUT /parametres/{cle} - Mettre à jour la valeur d'un paramètre
     */
    @PutMapping("/{cle}")
    public ResponseEntity<ApiResponse<Parametre>> updateValeur(
            @PathVariable String cle,
            @RequestParam String valeur) {
        try {
            Parametre parametre = parametreService.updateValeur(cle, valeur);
            return ResponseEntity.ok(
                ApiResponse.success(parametre, "Valeur mise à jour")
            );
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Erreur lors de la mise à jour", e.getMessage()));
        }
    }
    
    /**
     * DELETE /parametres/{id} - Supprimer un paramètre
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> supprimerParametre(@PathVariable Long id) {
        try {
            parametreService.supprimerParametre(id);
            return ResponseEntity.ok(
                ApiResponse.success(null, "Paramètre supprimé avec succès")
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Erreur lors de la suppression", e.getMessage()));
        }
    }
    
    /**
     * POST /parametres/initialiser - Initialiser les paramètres par défaut
     */
    @PostMapping("/initialiser")
    public ResponseEntity<ApiResponse<Void>> initialiserParametres() {
        try {
            parametreService.initialiserParametresParDefaut();
            return ResponseEntity.ok(
                ApiResponse.success(null, "Paramètres par défaut initialisés")
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Erreur lors de l'initialisation", e.getMessage()));
        }
    }
}