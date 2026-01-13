package com.example.projectflow.CONTROLLERS;

import com.example.projectflow.DTOs.*;
import com.example.projectflow.SERVICES.ProfesseurService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

@RestController
@RequestMapping("/api/professeur")

@CrossOrigin(origins = "http://localhost:5173")
public class ProfesseurController {

    private final ProfesseurService professeurService;

    public ProfesseurController(ProfesseurService professeurService) {
        this.professeurService = professeurService;
    }

    @GetMapping("/dashboard")
    public ResponseEntity<DashboardStatsDTO> getDashboardStats(HttpServletRequest request) {
        Integer professeurId = (Integer) request.getAttribute("userId");
        DashboardStatsDTO stats = professeurService.getDashboardStats(professeurId);
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/profile")
    public ResponseEntity<ProfesseurProfileDTO> getProfile(HttpServletRequest request) {
        Integer professeurId = (Integer) request.getAttribute("userId");
        ProfesseurProfileDTO profile = professeurService.getProfesseurProfile(professeurId);
        return ResponseEntity.ok(profile);
    }

    @PutMapping("/profile")
    public ResponseEntity<ProfesseurProfileDTO> updateProfile(
            @RequestBody ProfesseurProfileDTO dto,
            HttpServletRequest request) {
        Integer professeurId = (Integer) request.getAttribute("userId");
        ProfesseurProfileDTO updated = professeurService.updateProfesseurProfile(professeurId, dto);
        return ResponseEntity.ok(updated);
    }

    @PostMapping("/profile/photo")
    public ResponseEntity<Map<String, String>> uploadPhoto(
            @RequestParam("photo") MultipartFile file,
            HttpServletRequest request) {
        Integer professeurId = (Integer) request.getAttribute("userId");
        String photoUrl = professeurService.uploadProfilePhoto(professeurId, file);
        return ResponseEntity.ok(Map.of("photoUrl", photoUrl));
    }
}
