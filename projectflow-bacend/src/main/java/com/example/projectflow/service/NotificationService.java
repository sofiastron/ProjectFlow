package com.example.projectflow.service;

import com.example.projectflow.model.Notification;
import com.example.projectflow.model.Tache;
import com.example.projectflow.model.User;
import com.example.projectflow.DTOs.NotificationDTO;
import com.example.projectflow.repository.NotificationRepository;
import com.example.projectflow.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    // Créer une notification
    @Transactional
    public Notification creerNotification(Integer userId, String message, Tache tache) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'ID: " + userId));

        Notification notification = new Notification();
        notification.setMessage(message);
        notification.setDate(new Date());
        notification.setVu(false);
        notification.setUser(user);
        notification.setTache(tache);

        return notificationRepository.save(notification);
    }

    // Récupérer toutes les notifications d'un utilisateur
    public List<NotificationDTO> getNotifications(Integer userId) {
        List<Notification> notifications =
                notificationRepository.findByUserIdOrderByDateDesc(userId);

        return notifications.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Récupérer les notifications non lues d'un utilisateur
    public List<NotificationDTO> getNotificationsNonLues(Integer userId) {
        List<Notification> notifications =
                notificationRepository.findByUserIdAndVuFalseOrderByDateDesc(userId);

        return notifications.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Compter les notifications non lues
    public long compterNotificationsNonLues(Integer userId) {
        return notificationRepository.countByUserIdAndVuFalse(userId);
    }

    // Marquer une notification comme lue
    @Transactional
    public void marquerCommeLue(Integer notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification non trouvée avec l'ID: " + notificationId));

        notification.setVu(true);
        notificationRepository.save(notification);
    }

    // Marquer toutes les notifications comme lues
    @Transactional
    public void marquerToutesCommeLues(Integer userId) {
        List<Notification> notifications =
                notificationRepository.findByUserIdAndVuFalseOrderByDateDesc(userId);

        for (Notification notification : notifications) {
            notification.setVu(true);
            notificationRepository.save(notification);
        }
    }

    // Supprimer une notification
    public void deleteNotification(Integer id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification non trouvée"));
        notificationRepository.delete(notification);
    }

    // Conversion vers DTO
    private NotificationDTO convertToDTO(Notification notif) {
        NotificationDTO dto = new NotificationDTO();
        dto.setId(notif.getId());
        dto.setMessage(notif.getMessage());
        dto.setDate(notif.getDate());
        dto.setVu(notif.getVu());

        if (notif.getTache() != null) {
            dto.setTacheId(notif.getTache().getId());
            dto.setTacheTitre(notif.getTache().getTitre());

            if (notif.getTache().getSujet() != null &&
                    notif.getTache().getSujet().getEtudiant() != null) {
                dto.setEtudiantNom(notif.getTache().getSujet().getEtudiant().getNom());
            }
        }

        return dto;
    }
}