package com.example.projectflow.service;

import com.example.projectflow.model.Project;
import com.example.projectflow.repository.ProjectRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

@Service
public class DataLoaderService {

    @Autowired
    private ProjectRepository projectRepository;

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void loadDataFromJson() {
        System.out.println("🚀 Début du chargement des données JSON...");

        try {
            if (projectRepository.count() > 0) {
                System.out.println("✅ Données déjà présentes");
                return;
            }

            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            InputStream inputStream = getClass().getClassLoader()
                    .getResourceAsStream("projects.json");

            if (inputStream == null) {
                System.err.println("❌ Fichier projects.json non trouvé");
                return;
            }

            Project[] projects = mapper.readValue(inputStream, Project[].class);
            System.out.println("📋 " + projects.length + " projets lus");

            projectRepository.saveAll(Arrays.asList(projects));
            System.out.println("✅ " + projects.length + " projets chargés dans MySQL");

        } catch (Exception e) {
            System.err.println("❌ Erreur: " + e.getMessage());
            e.printStackTrace();
        }
    }
}