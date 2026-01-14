package com.example.projectflow.service;

import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class RapportService {

    public String uploadRapport(Long etudiantId, MultipartFile fichier) throws IOException {
        
        String nomFichier = fichier.getOriginalFilename();

        return "Rapport uploadé : " + nomFichier + " pour l'étudiant " + etudiantId;
    }
}
