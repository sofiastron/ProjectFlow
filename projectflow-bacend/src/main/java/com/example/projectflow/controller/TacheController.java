package com.example.projectflow.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.projectflow.entity.Tache;
import com.example.projectflow.service.TacheService;

@RestController
@RequestMapping("/api/taches")
public class TacheController {

    @Autowired
    private TacheService tacheService;

    @GetMapping("/etudiant/{etudiantId}/retard")
    public List<Tache> getTachesEnRetard(@PathVariable Long etudiantId) {
        return tacheService.getTachesByEtudiant(etudiantId);
    }

    @GetMapping("/etudiant/{etudiantId}/en-cours")
    public List<Tache> getTachesEnCours(@PathVariable Long etudiantId) {
        return tacheService.getTachesEnCours(etudiantId);
    }
}
