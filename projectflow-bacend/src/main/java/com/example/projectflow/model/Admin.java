package com.example.projectflow.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "admins")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Admin extends User {

    @Column(name = "field_type")
    private String fieldType;

    // Constructeur
    public Admin(Integer id, String nom, String email, String password, String fieldType) {
        super(id, nom, email, password, Role.ADMIN);
        this.fieldType = fieldType;
    }
}