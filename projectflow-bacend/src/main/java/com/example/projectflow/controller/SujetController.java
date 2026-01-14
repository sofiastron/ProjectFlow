package com.example.projectflow.controller;

import com.example.projectflow.entity.Sujet;
import com.example.projectflow.service.SujetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sujets")
public class SujetController {

    @Autowired
    private SujetService sujetService;

    @GetMapping("/etudiant/{etudiantId}")
    public List<Sujet> getSujetsByEtudiant(@PathVariable Long etudiantId) {
        return sujetService.getSujetsByEtudiant(etudiantId);
    }
}
