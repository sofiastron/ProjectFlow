package com.example.projectflow.SERVICES;

import com.example.projectflow.DTOs.DashboardStatsDTO;
import com.example.projectflow.DTOs.ProfesseurProfileDTO;
import org.springframework.web.multipart.MultipartFile;
import com.example.projectflow.DTOs.EtudiantDTO;
import java.util.List;

public interface ProfesseurService {
    DashboardStatsDTO getDashboardStats(Integer professeurId);
    ProfesseurProfileDTO getProfesseurProfile(Integer professeurId);
    ProfesseurProfileDTO updateProfesseurProfile(Integer professeurId, ProfesseurProfileDTO dto);
    String uploadProfilePhoto(Integer professeurId, MultipartFile file);
    List<EtudiantDTO> getEtudiantsAssignes(Integer professeurId);
}