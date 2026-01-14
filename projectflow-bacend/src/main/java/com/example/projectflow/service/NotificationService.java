package com.example.projectflow.service;

import com.example.projectflow.entity.Notification;
import com.example.projectflow.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepo;

    public List<Notification> getNotificationsNonLues(Long etudiantId) {
        return notificationRepo.findByUserIdAndVuFalse(etudiantId);
    }
}
