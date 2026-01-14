package com.example.projectflow.repository;

import com.example.projectflow.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    // Méthodes personnalisées si besoin
    // Project findByProjectId(String projectId);
}