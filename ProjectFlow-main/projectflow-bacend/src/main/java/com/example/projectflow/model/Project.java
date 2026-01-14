package com.example.projectflow.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.io.IOException;

@Entity
@Table(name = "projects")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "project_id")
    @JsonProperty("project_id")
    private String projectId;

    @Column(name = "title", length = 500)
    @JsonProperty("project_title")
    private String projectTitle;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    @JsonProperty("skills")
    @JsonDeserialize(using = SkillsDeserializer.class)
    private String skills;

    @Column(length = 500)
    private String url;

    // Champ temporaire pour le score (pas en base)
    @Transient
    private Integer score;

    // Constructeur
    public Project() {}

    // Getters et setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getProjectId() { return projectId; }
    public void setProjectId(String projectId) { this.projectId = projectId; }

    public String getProjectTitle() { return projectTitle; }
    public void setProjectTitle(String projectTitle) { this.projectTitle = projectTitle; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getSkills() { return skills; }
    public void setSkills(String skills) { this.skills = skills; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public Integer getScore() { return score; }
    public void setScore(Integer score) { this.score = score; }
}

class SkillsDeserializer extends JsonDeserializer<String> {
    @Override
    public String deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {

        JsonNode node = p.getCodec().readTree(p);

        if (node.isArray()) {
            StringBuilder sb = new StringBuilder();
            for (JsonNode skill : node) {
                if (sb.length() > 0) sb.append(",");
                sb.append(skill.asText());
            }
            return sb.toString();
        } else {
            return node.asText();
        }
    }
}