package com.example.projectflow.service;

import com.example.projectflow.dto.EncadrantDTO;
import com.example.projectflow.entity.Professeur;
import com.example.projectflow.repository.ProfesseurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EncadrantService {

	@Autowired
	private ProfesseurRepository professeurRepo;

	public List<Professeur> getAllEncadrants() {
		return professeurRepo.findAll();
	}

	public Professeur getById(Long id) {
		return professeurRepo.findById(id)
			.orElseThrow(() -> new RuntimeException("Encadrant non trouvé"));
	}

	// Utile pour afficher la spécialité ou le taux de charge dans le dashboard
	public List<Professeur> getEncadrantsParSpecialite(String specialite) {
		return professeurRepo.findBySpecialite(specialite);
	}





	public Professeur updateEncadrant(Long id, EncadrantDTO dto) {

		Professeur prof = professeurRepo.findById(id)
			.orElseThrow(() -> new RuntimeException("Encadrant non trouvé"));

		// Champs hérités de User
		prof.setNom(dto.getNom());
		prof.setEmail(dto.getEmail());

		// Champ spécifique Professeur
		prof.setSpecialite(dto.getSpecialite());

		// Champ logique (transient)
		if (dto.getCapaciteMax() != null) {
			prof.setCapaciteMax(dto.getCapaciteMax());
		}

		return professeurRepo.save(prof);
	}

	public void delete(Long id) {
		professeurRepo.deleteById(id);
	}


}
