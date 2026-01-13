package com.example.projectflow.SERVICES;

import com.example.projectflow.DTOs.*;
import com.example.projectflow.ENTITIES.*;
import com.example.projectflow.REPOSITORIES.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service

public class ProfesseurServiceImpl implements ProfesseurService {

    private final ProfesseurRepository professeurRepository;
    private final EtudiantRepository etudiantRepository;
    private final SujetRepository sujetRepository;
    private final UserRepository userRepository;

    @Value("${upload.path}")
    private String uploadPath;

    public ProfesseurServiceImpl(ProfesseurRepository professeurRepository, EtudiantRepository etudiantRepository, SujetRepository sujetRepository, UserRepository userRepository) {
        this.professeurRepository = professeurRepository;
        this.etudiantRepository = etudiantRepository;
        this.sujetRepository = sujetRepository;
        this.userRepository = userRepository;
    }


    // ===================== DASHBOARD =====================

    @Override
    @Transactional(readOnly = true)
    public DashboardStatsDTO getDashboardStats(Integer professeurId) {

        Professeur prof = professeurRepository.findById(professeurId)
                .orElseThrow(() -> new RuntimeException("Professeur non trouvé"));

        List<Etudiant> etudiants =
                etudiantRepository.findByProfesseurIdOrderByUserCreatedAtDesc(professeurId);

        List<Sujet> sujets =
                sujetRepository.findByProfesseurId(professeurId);

        int totalEtudiants = etudiants.size();
        int projetsActifs = (int) sujets.stream()
                .filter(s -> s.getEtudiant() != null)
                .count();

        long projetsTermines = sujets.stream()
                .filter(s -> s.getTaches() != null && !s.getTaches().isEmpty())
                .filter(s -> s.getTaches().stream()
                        .allMatch(t -> t.getEtat() == Tache.Etat.TERMINE))
                .count();

        return DashboardStatsDTO.builder()
                .professeurNom(prof.getUser().getNom())
                .totalEtudiants(totalEtudiants)
                .nouveauxEtudiants(2)
                .projetsActifs(projetsActifs)
                .projetsEnCours(sujets.size())
                .projetsTermines((int) projetsTermines)
                .nouveauxTermines(1)
                .projetsRevision(3)
                .statutsProjets(calculateStatutsProjets(sujets))
                .activiteHebdo(generateActiviteHebdo())
                .etudiantsRecents(getEtudiantsRecents(etudiants))
                .build();
    }

    // ===================== STATUTS =====================

    private List<StatutProjetDTO> calculateStatutsProjets(List<Sujet> sujets) {

        int total = sujets.isEmpty() ? 1 : sujets.size();

        long enCours = sujets.stream()
                .filter(s -> s.getTaches() != null && !s.getTaches().isEmpty())
                .filter(s -> s.getTaches().stream()
                        .anyMatch(t -> t.getEtat() == Tache.Etat.EN_COURS))
                .count();

        long termines = sujets.stream()
                .filter(s -> s.getTaches() != null && !s.getTaches().isEmpty())
                .filter(s -> s.getTaches().stream()
                        .allMatch(t -> t.getEtat() == Tache.Etat.TERMINE))
                .count();

        long enRetard = sujets.stream()
                .filter(s -> s.getTaches() != null && !s.getTaches().isEmpty())
                .filter(s -> s.getTaches().stream()
                        .anyMatch(t -> t.getEtat() == Tache.Etat.EN_RETARD))
                .count();

        return Arrays.asList(
                StatutProjetDTO.builder()
                        .type("En cours")
                        .count((int) enCours)
                        .pourcentage((double) enCours / total * 100)
                        .colorClass("bg-blue-500")
                        .build(),

                StatutProjetDTO.builder()
                        .type("Terminés")
                        .count((int) termines)
                        .pourcentage((double) termines / total * 100)
                        .colorClass("bg-green-500")
                        .build(),

                StatutProjetDTO.builder()
                        .type("En retard")
                        .count((int) enRetard)
                        .pourcentage((double) enRetard / total * 100)
                        .colorClass("bg-red-500")
                        .build()
        );
    }

    // ===================== ACTIVITÉ =====================

    private List<ActiviteJourDTO> generateActiviteHebdo() {
        return Arrays.asList(
                ActiviteJourDTO.builder().jour("Lun").valeur(15).build(),
                ActiviteJourDTO.builder().jour("Mar").valeur(22).build(),
                ActiviteJourDTO.builder().jour("Mer").valeur(18).build(),
                ActiviteJourDTO.builder().jour("Jeu").valeur(25).build(),
                ActiviteJourDTO.builder().jour("Ven").valeur(20).build(),
                ActiviteJourDTO.builder().jour("Sam").valeur(8).build(),
                ActiviteJourDTO.builder().jour("Dim").valeur(5).build()
        );
    }

    // ===================== ÉTUDIANTS =====================

    private List<EtudiantRecentDTO> getEtudiantsRecents(List<Etudiant> etudiants) {
        return etudiants.stream()
                .limit(5)
                .map(e -> EtudiantRecentDTO.builder()
                        .id(e.getId())
                        .nom(e.getUser().getNom())
                        .initiales(getInitiales(e.getUser().getNom()))
                        .filiere(e.getFiliere())
                        .dateAssignation(formatDate(e.getUser().getCreatedAt()))
                        .build())
                .collect(Collectors.toList());
    }

    private String getInitiales(String nom) {
        String[] parts = nom.split(" ");
        if (parts.length >= 2) {
            return (parts[0].charAt(0) + "" + parts[1].charAt(0)).toUpperCase();
        }
        return nom.substring(0, Math.min(2, nom.length())).toUpperCase();
    }

    private String formatDate(java.time.LocalDateTime date) {
        return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    // ===================== PROFIL =====================

    @Override
    @Transactional(readOnly = true)
    public ProfesseurProfileDTO getProfesseurProfile(Integer professeurId) {

        Professeur prof = professeurRepository.findById(professeurId)
                .orElseThrow(() -> new RuntimeException("Professeur non trouvé"));

        return ProfesseurProfileDTO.builder()
                .id(prof.getId())
                .nom(prof.getUser().getNom())
                .email(prof.getUser().getEmail())
                .specialite(prof.getSpecialite())
                .role("Professeur")
                .createdAt(prof.getUser().getCreatedAt())
                .photo(null)
                .build();
    }

    @Override
    @Transactional
    public ProfesseurProfileDTO updateProfesseurProfile(
            Integer professeurId, ProfesseurProfileDTO dto) {

        Professeur prof = professeurRepository.findById(professeurId)
                .orElseThrow(() -> new RuntimeException("Professeur non trouvé"));

        User user = prof.getUser();
        user.setNom(dto.getNom());
        user.setEmail(dto.getEmail());
        userRepository.save(user);

        prof.setSpecialite(dto.getSpecialite());
        professeurRepository.save(prof);

        return getProfesseurProfile(professeurId);
    }

    // ===================== UPLOAD =====================

    @Override
    @Transactional
    public String uploadProfilePhoto(Integer professeurId, MultipartFile file) {

        try {
            String fileName = professeurId + "_" + System.currentTimeMillis()
                    + getFileExtension(file.getOriginalFilename());

            Path uploadDir = Paths.get(uploadPath);
            Files.createDirectories(uploadDir);

            Path filePath = uploadDir.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return "/uploads/photos/" + fileName;

        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de l'upload de la photo", e);
        }
    }

    private String getFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) return "";
        return filename.substring(filename.lastIndexOf("."));
    }
}
