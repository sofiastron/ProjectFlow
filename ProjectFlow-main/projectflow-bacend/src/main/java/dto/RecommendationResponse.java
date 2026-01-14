package com.example.projectflow.dto;

import java.util.List;

public class RecommendationResponse {
    private List<ProjectDTO> projects;
    private int total;
    private String searchQuery;

    // Constructeurs, getters, setters
    public RecommendationResponse(List<ProjectDTO> projects, int total, String searchQuery) {
        this.projects = projects;
        this.total = total;
        this.searchQuery = searchQuery;
    }

    // Getters et setters
    public List<ProjectDTO> getProjects() { return projects; }
    public void setProjects(List<ProjectDTO> projects) { this.projects = projects; }

    public int getTotal() { return total; }
    public void setTotal(int total) { this.total = total; }

    public String getSearchQuery() { return searchQuery; }
    public void setSearchQuery(String searchQuery) { this.searchQuery = searchQuery; }
}