package com.example.projectflow.service;

import com.example.projectflow.model.Project;
import com.example.projectflow.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

    @Autowired
    private ProjectRepository projectRepository;

    public List<Project> getProjectsBySkills(String skillsInput) {
        if (skillsInput == null || skillsInput.trim().isEmpty()) {
            return Collections.emptyList();
        }

        // Nettoyer et normaliser les compétences
        List<String> inputSkills = Arrays.stream(skillsInput.toLowerCase().split("[,\\s]+"))
                .map(String::trim)
                .filter(skill -> !skill.isEmpty())
                .collect(Collectors.toList());

        System.out.println("🔍 Compétences recherchées: " + inputSkills);

        List<Project> allProjects = projectRepository.findAll();
        List<ProjectWithScore> scoredProjects = new ArrayList<>();

        for (Project project : allProjects) {
            if (project.getSkills() == null || project.getSkills().isEmpty()) {
                continue;
            }

            String projectSkills = project.getSkills().toLowerCase();
            int score = 0;

            // Pour chaque compétence recherchée
            for (String inputSkill : inputSkills) {
                if (isExactMatch(projectSkills, inputSkill)) {
                    score += 3; // Bonus pour correspondance exacte
                } else if (isWordMatch(projectSkills, inputSkill)) {
                    score += 2; // Correspondance de mot complet
                } else if (containsSkill(projectSkills, inputSkill)) {
                    score += 1; // Correspondance partielle
                }
            }

            // Pénaliser les faux positifs (ex: "java" dans "javascript")
            if (hasFalsePositive(projectSkills, inputSkills)) {
                score -= 5; // Pénalité sévère pour faux positif
            }

            if (score > 0) {
                ProjectWithScore pws = new ProjectWithScore(project, score);
                scoredProjects.add(pws);
            }
        }

        // Trier par score décroissant
        scoredProjects.sort((a, b) -> b.score - a.score);

        // Filtrer pour éviter les faux positifs
        List<ProjectWithScore> filtered = scoredProjects.stream()
                .filter(pws -> !isLikelyFalsePositive(pws.project.getSkills().toLowerCase(), inputSkills))
                .limit(6)
                .collect(Collectors.toList());

        // Ajouter les scores aux projets
        return filtered.stream()
                .map(pws -> {
                    pws.project.setScore(pws.score);
                    return pws.project;
                })
                .collect(Collectors.toList());
    }

    // Vérifie si c'est une correspondance exacte (mot entier séparé par virgule)
    private boolean isExactMatch(String projectSkills, String inputSkill) {
        // Recherche du mot exact dans la liste de compétences
        String[] skillsArray = projectSkills.split("[,;]\\s*");
        for (String skill : skillsArray) {
            if (skill.trim().equalsIgnoreCase(inputSkill)) {
                return true;
            }
        }
        return false;
    }

    // Vérifie si le mot apparaît comme mot complet
    private boolean isWordMatch(String projectSkills, String inputSkill) {
        // Utilise des regex pour détecter les mots complets
        Pattern pattern = Pattern.compile("\\b" + Pattern.quote(inputSkill) + "\\b",
                Pattern.CASE_INSENSITIVE);
        return pattern.matcher(projectSkills).find();
    }

    // Vérifie si le mot apparaît (même partiellement)
    private boolean containsSkill(String projectSkills, String inputSkill) {
        return projectSkills.contains(inputSkill);
    }

    // Détecte les faux positifs courants
    private boolean hasFalsePositive(String projectSkills, List<String> inputSkills) {
        // Règles pour éviter les faux positifs
        for (String inputSkill : inputSkills) {
            if (inputSkill.equals("java")) {
                // Si on cherche "java" mais que le projet a "javascript" sans "java"
                if (projectSkills.contains("javascript") &&
                        !isWordMatch(projectSkills, "java") &&
                        !isExactMatch(projectSkills, "java")) {
                    return true;
                }
            }

            if (inputSkill.equals("c")) {
                // Si on cherche "c" mais que le projet a "c++" ou "c#" sans "c" seul
                if ((projectSkills.contains("c++") || projectSkills.contains("c#")) &&
                        !isWordMatch(projectSkills, "c")) {
                    return true;
                }
            }
        }
        return false;
    }

    // Vérifie si c'est probablement un faux positif
    private boolean isLikelyFalsePositive(String projectSkills, List<String> inputSkills) {
        for (String inputSkill : inputSkills) {
            // Cas spécifique: recherche "java" mais projet n'a que "javascript"
            if (inputSkill.equals("java") && projectSkills.contains("javascript")) {
                // Vérifier si le projet a vraiment "java" ou seulement "javascript"
                String[] skills = projectSkills.split("[,;]\\s*");
                boolean hasJava = false;
                boolean hasJavascript = false;

                for (String skill : skills) {
                    if (skill.trim().equalsIgnoreCase("java")) {
                        hasJava = true;
                    }
                    if (skill.trim().equalsIgnoreCase("javascript")) {
                        hasJavascript = true;
                    }
                }

                // Si le projet a "javascript" mais pas "java", c'est un faux positif
                if (hasJavascript && !hasJava) {
                    System.out.println("⚠️ Exclu faux positif: javascript sans java");
                    return true;
                }
            }

            // Autres cas de faux positifs potentiels
            if (inputSkill.equals("script") && projectSkills.contains("javascript")) {
                // Recherche "script" mais projet a "javascript"
                if (!isExactMatch(projectSkills, "script")) {
                    return true;
                }
            }
        }
        return false;
    }

    // Classe interne pour le score
    private static class ProjectWithScore {
        Project project;
        int score;

        ProjectWithScore(Project project, int score) {
            this.project = project;
            this.score = score;
        }
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public long countProjects() {
        return projectRepository.count();
    }
}