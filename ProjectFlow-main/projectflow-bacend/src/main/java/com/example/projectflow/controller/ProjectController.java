package com.example.projectflow.controller;

import com.example.projectflow.model.Project;
import com.example.projectflow.service.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class ProjectController {

    @Autowired
    private RecommendationService recommendationService;

    @PostMapping("/recommend")
    public List<Project> getRecommendations(@RequestBody Map<String, String> request) {
        String skills = request.get("skills");
        System.out.println("🔍 Recherche avec: " + skills);

        List<Project> results = recommendationService.getProjectsBySkills(skills);
        System.out.println("✅ " + results.size() + " résultats");

        return results.size() > 6 ? results.subList(0, 6) : results;
    }

    @GetMapping("/test")
    public String test() {
        return "✅ Backend fonctionne sur port 8000 !";
    }

    @GetMapping("/count")
    public Map<String, Long> count() {
        return Map.of("count", recommendationService.countProjects());
    }

    @GetMapping("/projects")
    public List<Project> getAll() {
        return recommendationService.getAllProjects();
    }
}