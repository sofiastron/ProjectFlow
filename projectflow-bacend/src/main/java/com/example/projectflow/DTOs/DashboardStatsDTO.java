package com.example.projectflow.DTOs;

import lombok.*;
import java.util.List;

@Data
@Builder
public class DashboardStatsDTO {
    private String professeurNom;
    private Integer totalEtudiants;
    private Integer nouveauxEtudiants;
    private Integer projetsActifs;
    private Integer projetsEnCours;
    private Integer projetsTermines;
    private Integer nouveauxTermines;
    private Integer projetsRevision;
    private List<StatutProjetDTO> statutsProjets;
    private List<ActiviteJourDTO> activiteHebdo;
    private List<EtudiantRecentDTO> etudiantsRecents;
}
