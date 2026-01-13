package com.example.projectflow.controller;

import com.example.projectflow.model.Sujet;
import com.example.projectflow.service.SujetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/sujets")
@CrossOrigin(origins = "*")
public class SujetController {

    @Autowired
    private SujetService sujetService;

    /**
     * GET /api/sujets
     * Récupérer tous les sujets
     */
    @GetMapping
    public ResponseEntity<List<Sujet>> getAllSujets() {
        try {
            List<Sujet> sujets = sujetService.getAllSujets();
            return ResponseEntity.ok(sujets);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * GET /api/sujets/{id}
     * Récupérer un sujet par son ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Sujet> getSujetById(@PathVariable Integer id) {
        try {
            Sujet sujet = sujetService.getSujetById(id);
            return ResponseEntity.ok(sujet);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * GET /api/sujets/professeur/{profId}
     * Récupérer tous les sujets d'un professeur
     */
    @GetMapping("/professeur/{profId}")
    public ResponseEntity<List<Sujet>> getSujetsByProfesseur(
            @PathVariable Integer profId) {
        try {
            List<Sujet> sujets = sujetService.getSujetsByProfesseur(profId);
            return ResponseEntity.ok(sujets);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * GET /api/sujets/etudiant/{etudiantId}
     * Récupérer le sujet d'un étudiant
     */
    @GetMapping("/etudiant/{etudiantId}")
    public ResponseEntity<Sujet> getSujetByEtudiant(@PathVariable Integer etudiantId) {
        try {
            Sujet sujet = sujetService.getSujetByEtudiant(etudiantId);
            if (sujet == null) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(sujet);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * POST /api/sujets
     * Créer un nouveau sujet
     * Body: { "titre": "...", "description": "...", "professeur": { "id": 1 } }
     */
    @PostMapping
    public ResponseEntity<Sujet> createSujet(@RequestBody Sujet sujet) {
        try {
            Sujet newSujet = sujetService.createSujet(sujet);
            return ResponseEntity.status(HttpStatus.CREATED).body(newSujet);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * PUT /api/sujets/{id}
     * Mettre à jour un sujet existant
     */
    @PutMapping("/{id}")
    public ResponseEntity<Sujet> updateSujet(
            @PathVariable Integer id,
            @RequestBody Sujet sujet) {
        try {
            Sujet updatedSujet = sujetService.updateSujet(id, sujet);
            return ResponseEntity.ok(updatedSujet);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * PUT /api/sujets/{sujetId}/affecter/{etudiantId}
     * Affecter un sujet à un étudiant
     */
    @PutMapping("/{sujetId}/affecter/{etudiantId}")
    public ResponseEntity<Sujet> affecterSujet(
            @PathVariable Integer sujetId,
            @PathVariable Integer etudiantId) {
        try {
            Sujet sujet = sujetService.affecterSujet(sujetId, etudiantId);
            return ResponseEntity.ok(sujet);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
    }

    /**
     * PUT /api/sujets/{sujetId}/desaffecter
     * Désaffecter un étudiant d'un sujet
     */
    @PutMapping("/{sujetId}/desaffecter")
    public ResponseEntity<Sujet> desaffecterSujet(@PathVariable Integer sujetId) {
        try {
            Sujet sujet = sujetService.getSujetById(sujetId);
            sujet.setEtudiant(null);
            Sujet updatedSujet = sujetService.updateSujet(sujetId, sujet);
            return ResponseEntity.ok(updatedSujet);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * GET /api/sujets/{sujetId}/statistiques
     * Récupérer les statistiques d'un sujet (nombre de tâches par état)
     */
    @GetMapping("/{sujetId}/statistiques")
    public ResponseEntity<Map<String, Object>> getStatistiquesSujet(
            @PathVariable Integer sujetId) {
        try {
            Sujet sujet = sujetService.getSujetById(sujetId);

            Map<String, Object> stats = new HashMap<>();
            stats.put("sujetId", sujet.getId());
            stats.put("titre", sujet.getTitre());

            // Compter les tâches par état
            long aFaire = sujet.getTaches() != null ?
                    sujet.getTaches().stream()
                            .filter(t -> t.getEtat().toString().equals("A_FAIRE"))
                            .count() : 0;

            long enCours = sujet.getTaches() != null ?
                    sujet.getTaches().stream()
                            .filter(t -> t.getEtat().toString().equals("EN_COURS"))
                            .count() : 0;

            long aVerifier = sujet.getTaches() != null ?
                    sujet.getTaches().stream()
                            .filter(t -> t.getEtat().toString().equals("A_VERIFIER"))
                            .count() : 0;

            long termine = sujet.getTaches() != null ?
                    sujet.getTaches().stream()
                            .filter(t -> t.getEtat().toString().equals("TERMINE"))
                            .count() : 0;

            long total = sujet.getTaches() != null ? sujet.getTaches().size() : 0;

            Map<String, Long> tachesParEtat = new HashMap<>();
            tachesParEtat.put("A_FAIRE", aFaire);
            tachesParEtat.put("EN_COURS", enCours);
            tachesParEtat.put("A_VERIFIER", aVerifier);
            tachesParEtat.put("TERMINE", termine);
            tachesParEtat.put("TOTAL", total);

            stats.put("taches", tachesParEtat);

            // Calcul du pourcentage de complétion
            double pourcentage = total > 0 ? (double) termine / total * 100 : 0;
            stats.put("pourcentageCompletion", Math.round(pourcentage * 100.0) / 100.0);

            return ResponseEntity.ok(stats);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * GET /api/sujets/disponibles
     * Récupérer tous les sujets non affectés (sans étudiant)
     */
    @GetMapping("/disponibles")
    public ResponseEntity<List<Sujet>> getSujetsDisponibles() {
        try {
            List<Sujet> sujets = sujetService.getAllSujets();
            List<Sujet> sujetsDisponibles = sujets.stream()
                    .filter(s -> s.getEtudiant() == null)
                    .toList();
            return ResponseEntity.ok(sujetsDisponibles);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * GET /api/sujets/affectes
     * Récupérer tous les sujets affectés (avec un étudiant)
     */
    @GetMapping("/affectes")
    public ResponseEntity<List<Sujet>> getSujetsAffectes() {
        try {
            List<Sujet> sujets = sujetService.getAllSujets();
            List<Sujet> sujetsAffectes = sujets.stream()
                    .filter(s -> s.getEtudiant() != null)
                    .toList();
            return ResponseEntity.ok(sujetsAffectes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * GET /api/sujets/recherche
     * Rechercher des sujets par titre
     * Query param: ?titre=...
     */
    @GetMapping("/recherche")
    public ResponseEntity<List<Sujet>> rechercherSujets(
            @RequestParam(required = false) String titre) {
        try {
            List<Sujet> sujets = sujetService.getAllSujets();

            if (titre != null && !titre.isEmpty()) {
                sujets = sujets.stream()
                        .filter(s -> s.getTitre().toLowerCase()
                                .contains(titre.toLowerCase()))
                        .toList();
            }

            return ResponseEntity.ok(sujets);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * GET /api/sujets/{sujetId}/existe
     * Vérifier si un sujet existe
     */
    @GetMapping("/{sujetId}/existe")
    public ResponseEntity<Map<String, Boolean>> verifierExistence(
            @PathVariable Integer sujetId) {
        try {
            sujetService.getSujetById(sujetId);
            Map<String, Boolean> response = new HashMap<>();
            response.put("existe", true);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Boolean> response = new HashMap<>();
            response.put("existe", false);
            return ResponseEntity.ok(response);
        }
    }

    /**
     * DELETE /api/sujets/{id}
     * Supprimer un sujet
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteSujet(@PathVariable Integer id) {
        try {
            sujetService.deleteSujet(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Sujet supprimé avec succès");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Sujet non trouvé");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    /**
     * GET /api/sujets/count
     * Compter le nombre total de sujets
     */
    @GetMapping("/count")
    public ResponseEntity<Map<String, Long>> countSujets() {
        try {
            long count = sujetService.getAllSujets().size();
            Map<String, Long> response = new HashMap<>();
            response.put("total", count);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * GET /api/sujets/professeur/{profId}/count
     * Compter le nombre de sujets d'un professeur
     */
    @GetMapping("/professeur/{profId}/count")
    public ResponseEntity<Map<String, Long>> countSujetsByProfesseur(
            @PathVariable Integer profId) {
        try {
            long count = sujetService.getSujetsByProfesseur(profId).size();
            Map<String, Long> response = new HashMap<>();
            response.put("total", count);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}