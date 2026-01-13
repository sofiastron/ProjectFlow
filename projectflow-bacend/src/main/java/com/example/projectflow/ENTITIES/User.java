package com.example.projectflow.ENTITIES;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


import java.time.LocalDateTime;

@Entity
@Table(name = "user")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;

        @Column(nullable = false)
        private String nom;

        @Column(nullable = false, unique = true)
        private String email;

        @Column(nullable = false)
        private String pasword;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private Role role;

        @Column(name = "created_at")
        private LocalDateTime createdAt;

        @Column(name = "updated_at")
        private LocalDateTime updatedAt;

        @PrePersist
        protected void onCreate() {
            createdAt = LocalDateTime.now();
            updatedAt = LocalDateTime.now();
        }

        @PreUpdate
        protected void onUpdate() {
            updatedAt = LocalDateTime.now();
        }

    public String getPasword() {
            return this.pasword;
    }

    public String getEmail() {
            return this.email;
    }

    public Role getRole() {
            return this.role;
    }

    public void setNom(String nom) {
            this.nom=nom;
    }

    public void setEmail(String email) {
            this.email=email;
    }

    public LocalDateTime getCreatedAt() {
            return this.createdAt;
    }

    public String getNom() {
            return this.nom;
    }

    public Integer getId() {
            return this.id;
    }

    public enum Role {
            Admin, Professeur, Etudiant
        }
    }


