package com.example.projectflow.DTOs;

import java.time.LocalDateTime;

public class ProfesseurProfileDTO {
    private Integer id;
    private String nom;
    private String email;
    private String specialite;
    private String role;
    private LocalDateTime createdAt;
    private String photo;

    public ProfesseurProfileDTO() {}

    public ProfesseurProfileDTO(Integer id, String nom, String email, String specialite,
                                String role, LocalDateTime createdAt, String photo) {
        this.id = id;
        this.nom = nom;
        this.email = email;
        this.specialite = specialite;
        this.role = role;
        this.createdAt = createdAt;
        this.photo = photo;
    }

    // Getters et Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSpecialite() { return specialite; }
    public void setSpecialite(String specialite) { this.specialite = specialite; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public String getPhoto() { return photo; }
    public void setPhoto(String photo) { this.photo = photo; }

    // Builder Pattern (optionnel mais utile)
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Integer id;
        private String nom;
        private String email;
        private String specialite;
        private String role;
        private LocalDateTime createdAt;
        private String photo;

        public Builder id(Integer id) { this.id = id; return this; }
        public Builder nom(String nom) { this.nom = nom; return this; }
        public Builder email(String email) { this.email = email; return this; }
        public Builder specialite(String specialite) { this.specialite = specialite; return this; }
        public Builder role(String role) { this.role = role; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public Builder photo(String photo) { this.photo = photo; return this; }

        public ProfesseurProfileDTO build() {
            return new ProfesseurProfileDTO(id, nom, email, specialite, role, createdAt, photo);
        }
    }
}