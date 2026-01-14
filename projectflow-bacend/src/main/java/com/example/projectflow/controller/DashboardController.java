package com.example.projectflow.controller;

import com.example.projectflow.dto.ApiResponse;
import com.example.projectflow.dto.DashboardStatsDTO;
import com.example.projectflow.repository.EtudiantRepository;
import com.example.projectflow.repository.ProfesseurRepository;
import com.example.projectflow.repository.RapportRepository;
import com.example.projectflow.repository.SoutenanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/dashboard")
@CrossOrigin("*")
public class DashboardController {

	@Autowired
	private RapportRepository rapportRepository;

	@Autowired
	private EtudiantRepository etudiantRepository;

	@Autowired
	private ProfesseurRepository professeurRepository;

	@Autowired
	private SoutenanceRepository soutenanceRepository;

	/**
	 * Récupère les chiffres pour les 4 cartes de couleur en haut du dashboard
	 */
	@GetMapping("/stats")
	public ResponseEntity<ApiResponse<DashboardStatsDTO>> getDashboardStats() {
		DashboardStatsDTO stats = new DashboardStatsDTO(
			rapportRepository.count(),
			rapportRepository.countByStatut("Validé"),
			rapportRepository.countByStatut("En cours"),
			rapportRepository.countByStatut("À corriger")
		);
		return ResponseEntity.ok(
			ApiResponse.success(stats, "Statistiques du dashboard")
		);
	}

	/**
	 * Récupère des statistiques globales pour les graphiques (étudiants vs professeurs)
	 */
	@GetMapping("/overview")
	public ResponseEntity<ApiResponse<Map<String, Long>>> getOverview() {
		Map<String, Long> overview = new HashMap<>();
		overview.put("totalEtudiants", etudiantRepository.count());
		overview.put("totalProfesseurs", professeurRepository.count());
		overview.put("totalSoutenances", soutenanceRepository.count());
		return ResponseEntity.ok(ApiResponse.success(overview, "Statistiques générales"));
	}
}
