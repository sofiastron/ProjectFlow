package com.example.projectflow.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.projectflow.entity.Notification;
import com.example.projectflow.service.NotificationService;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/etudiant/{etudiantId}/non-lues")
    public List<Notification> getNotificationsNonLues(@PathVariable Long etudiantId) {
        return notificationService.getNotificationsNonLues(etudiantId);
    }
}
