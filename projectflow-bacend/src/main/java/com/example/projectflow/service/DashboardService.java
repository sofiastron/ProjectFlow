package com.example.projectflow.service;

import com.example.projectflow.dto.DashboardStatsDTO;
import com.example.projectflow.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class DashboardService {

	@Autowired private RapportRepository rapportRepo;
	@Autowired private EtudiantRepository etudiantRepo;
	@Autowired private ProfesseurRepository professeurRepo;
	@Autowired private SoutenanceRepository soutenanceRepo;

	public DashboardStatsDTO getRapportStats() {
		return new DashboardStatsDTO(
			rapportRepo.count(),                    // Total (Bleu)
			rapportRepo.countByStatut("Validé"),    // Vert
			rapportRepo.countByStatut("En cours"),  // Jaune
			rapportRepo.countByStatut("À corriger") // Rouge
		);
	}

	public Map<String, Long> getGlobalOverview() {
		Map<String, Long> overview = new HashMap<>();
		overview.put("totalEtudiants", etudiantRepo.count());
		overview.put("totalProfesseurs", professeurRepo.count());
		overview.put("totalSoutenances", soutenanceRepo.count());
		return overview;
	}
}
