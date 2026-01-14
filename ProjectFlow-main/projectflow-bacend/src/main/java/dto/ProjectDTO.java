package com.example.projectflow.dto;

public class ProjectDTO {
    private String projectTitle;
    private String description;
    private String skills;
    private String url;
    private int score;

    // Constructeurs, getters, setters
    public ProjectDTO(String projectTitle, String description, String skills, String url, int score) {
        this.projectTitle = projectTitle;
        this.description = description;
        this.skills = skills;
        this.url = url;
        this.score = score;
    }

    // Getters et setters
    public String getProjectTitle() { return projectTitle; }
    public void setProjectTitle(String projectTitle) { this.projectTitle = projectTitle; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getSkills() { return skills; }
    public void setSkills(String skills) { this.skills = skills; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }
}