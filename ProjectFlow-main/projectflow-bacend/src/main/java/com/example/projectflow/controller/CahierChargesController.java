package com.example.projectflow.controller;

import com.example.projectflow.model.CahierCharges;
import com.example.projectflow.service.CahierChargesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/cahier-charges")
@CrossOrigin(origins = "*")
public class CahierChargesController {

    @Autowired
    private CahierChargesService service;

    /**
     * 1. Sauvegarder avec PDF
     */
    @PostMapping("/save-with-pdf")
    public ResponseEntity<Map<String, Object>> saveWithPdf(@RequestBody CahierCharges cahier) {
        try {
            System.out.println("📄 Sauvegarde avec PDF...");

            CahierCharges saved = service.saveWithPdf(cahier);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("id", saved.getId());
            response.put("message", "Sauvegardé avec PDF");
            response.put("fichierPdf", saved.getFichierPdf());
            response.put("pdfGenerated", saved.getFichierPdf() != null);
            response.put("dateCreation", saved.getDateCreation());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    /**
     * 2. Télécharger le PDF
     */
    @GetMapping("/download-pdf/{id}")
    public ResponseEntity<InputStreamResource> downloadPdf(@PathVariable Long id) {
        try {
            Optional<CahierCharges> cahierOpt = service.findById(id);

            if (cahierOpt.isEmpty() || cahierOpt.get().getFichierPdf() == null) {
                return ResponseEntity.notFound().build();
            }

            CahierCharges cahier = cahierOpt.get();
            String pdfPath = cahier.getFichierPdf();

            File pdfFile = new File(pdfPath);
            if (!pdfFile.exists()) {
                System.err.println("❌ Fichier PDF non trouvé: " + pdfPath);
                return ResponseEntity.notFound().build();
            }

            System.out.println("📥 Téléchargement PDF: " + pdfPath);

            FileInputStream fileInputStream = new FileInputStream(pdfFile);
            InputStreamResource resource = new InputStreamResource(fileInputStream);

            String fileName = "cahier_charges_" + cahier.getId() + "_" +
                    cahier.getSujetProjet().replaceAll("[^a-zA-Z0-9.-]", "_") + ".pdf";

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + fileName + "\"")
                    .contentType(MediaType.APPLICATION_PDF)
                    .contentLength(pdfFile.length())
                    .body(resource);

        } catch (FileNotFoundException e) {
            System.err.println("❌ Fichier non trouvé: " + e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            System.err.println("❌ Erreur téléchargement: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * 3. Tester la connexion
     */
    @GetMapping("/test")
    public ResponseEntity<Map<String, String>> test() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "OK");
        response.put("message", "API fonctionne");
        return ResponseEntity.ok(response);
    }

    /**
     * 4. Récupérer tous les cahiers - CORRIGÉ
     */
    @GetMapping("/all")
    public ResponseEntity<?> getAllCahiers() {
        try {
            List<CahierCharges> cahiers = service.getAllCahiers();
            System.out.println("📋 Récupération de " + cahiers.size() + " cahiers");

            // Ajoutez un statut par défaut pour le frontend
            for (CahierCharges cahier : cahiers) {
                // Ne faites rien - le modèle gère déjà le statut par défaut
            }

            return ResponseEntity.ok(cahiers);
        } catch (Exception e) {
            System.err.println("❌ Erreur récupération cahiers: " + e.getMessage());

            // En cas d'erreur, retournez un tableau vide
            return ResponseEntity.ok(List.of());
        }
    }

    /**
     * 5. Récupérer un cahier par ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getCahierById(@PathVariable Long id) {
        try {
            Optional<CahierCharges> cahierOpt = service.findById(id);
            if (cahierOpt.isPresent()) {
                return ResponseEntity.ok(cahierOpt.get());
            } else {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Cahier non trouvé avec ID: " + id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }
        } catch (Exception e) {
            System.err.println("❌ Erreur récupération cahier: " + e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", "Erreur serveur");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * 6. Valider un cahier - SIMULATION
     */
    @PutMapping("/{id}/valider")
    public ResponseEntity<?> validerCahier(@PathVariable Long id) {
        try {
            // Simulation seulement - pas de vrai update car pas de colonne statut
            System.out.println("✅ Simulation validation cahier ID: " + id);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Cahier marqué comme validé (simulation)");
            response.put("id", id);
            response.put("statut", "validé");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.err.println("❌ Erreur validation: " + e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", "Erreur lors de la validation");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * 7. Supprimer un cahier - SIMULATION
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCahier(@PathVariable Long id) {
        try {
            // Simulation seulement
            System.out.println("🗑️ Simulation suppression cahier ID: " + id);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Cahier marqué comme supprimé (simulation)");
            response.put("id", id);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.err.println("❌ Erreur suppression: " + e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", "Erreur lors de la suppression");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * 8. Dévalider un cahier - SIMULATION
     */
    @PutMapping("/{id}/invalider")
    public ResponseEntity<?> invaliderCahier(@PathVariable Long id) {
        try {
            // Simulation seulement
            System.out.println("↩️ Simulation dévalidation cahier ID: " + id);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Cahier marqué comme non validé (simulation)");
            response.put("id", id);
            response.put("statut", "en_attente");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.err.println("❌ Erreur dévalidation: " + e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", "Erreur lors de la dévalidation");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}