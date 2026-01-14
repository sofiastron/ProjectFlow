package com.example.projectflow.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.projectflow.dto.DashboardDTO;
import com.example.projectflow.repository.MessageRepository;
import com.example.projectflow.repository.NotificationRepository;
import com.example.projectflow.repository.SujetRepository;
import com.example.projectflow.repository.TacheRepository;

@Service
public class DashboardService {

    @Autowired
    private SujetRepository sujetRepo;

    @Autowired
    private TacheRepository tacheRepo;

    @Autowired
    private MessageRepository messageRepo;

    @Autowired
    private NotificationRepository notificationRepo;

    public DashboardDTO getDashboard(Long etudiantId) {
        
        int projetsTermines = sujetRepo.countByEtudiantIdAndProgression(etudiantId, 100);

        int projetsEnCours = sujetRepo.countByEtudiantIdAndProgressionLessThan(etudiantId, 100);


        int tachesEnRetard = tacheRepo.countBySujetEtudiantIdAndEtatNotAndDateFinBefore(
                etudiantId, "Terminé", LocalDate.now()
        );

        int messagesNonLus = messageRepo.countByDestinataireIdAndLuFalse(etudiantId);

        int notificationsNonLues = notificationRepo.countByUserIdAndVuFalse(etudiantId);

        return new DashboardDTO(projetsTermines, projetsEnCours, tachesEnRetard, messagesNonLus, notificationsNonLues);
    }
}
