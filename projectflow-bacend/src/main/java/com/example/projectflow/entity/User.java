package com.example.projectflow.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;

@Entity
@Table(name = "user")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 100)
	private String nom;

	@Column(nullable = false, unique = true, length = 150)
	private String email;

	@Column(nullable = false, length = 255)
	@JsonIgnore // Sécurité : Ne jamais afficher le mot de passe dans le JSON
	private String pasword;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Role role;

	@CreationTimestamp
	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@UpdateTimestamp
	@Column(name = "updated_at", nullable = false)
	private LocalDateTime updatedAt;

	public enum Role {
		Admin,
		Professeur,
		Etudiant
	}
}
