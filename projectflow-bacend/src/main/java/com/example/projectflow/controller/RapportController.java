package com.example.projectflow.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.projectflow.service.RapportService;

@RestController
@RequestMapping("/api/rapports")
public class RapportController {

    @Autowired
    private RapportService rapportService;

    @PostMapping("/upload/{etudiantId}")
    public ResponseEntity<String> uploadRapport(
            @PathVariable Long etudiantId,
            @RequestParam("fichier") MultipartFile fichier) {

        try {
            
            String message = rapportService.uploadRapport(etudiantId, fichier);
            return ResponseEntity.ok(message);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de l'upload du fichier : " + e.getMessage());
        }
    }
}
