package com.example.projectflow.service;

import com.example.projectflow.dto.RapportDTO;
import com.example.projectflow.entity.Rapport;
import com.example.projectflow.repository.RapportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RapportService {

	@Autowired
	private RapportRepository rapportRepo; // L'instance injectée

	// Méthode pour transformer un Rapport en RapportDTO
    public RapportDTO toDTO(Rapport r) {
        return new RapportDTO(
            r.getId(),
            r.getTitre(),
            r.getFichierPath(),
            r.getDateDepot(),
            r.getStatut(),
            r.getEtudiant().getNom(),
            r.getProfesseur().getNom(),
            r.getSujet() != null ? r.getSujet().getTitre() : null
        );
    }

	// Pour renvoyer la liste des DTO
    public List<RapportDTO> getAllRapportsDTO() {
        return rapportRepo.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

	public Rapport saveRapport(Rapport rapport) {
		// Utilise l'instance 'rapportRepo' et non la classe 'RapportRepository'
		return rapportRepo.save(rapport);
	}

	public void updateStatut(Long id, String nouveauStatut) {
		Rapport rapport = rapportRepo.findById(id)
			.orElseThrow(() -> new RuntimeException("Rapport non trouvé avec l'id : " + id));

		rapport.setStatut(nouveauStatut); // Si c'est rouge, vérifiez Rapport.java
		rapportRepo.save(rapport);
	}

	public void validerParEncadrant(Long id) {
		updateStatut(id, "Validé");
	}

	public void demanderCorrection(Long id) {
		updateStatut(id, "À corriger");
	}
}
