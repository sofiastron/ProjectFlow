package com.example.projectflow.controller;

import com.example.projectflow.DTOs.NotificationDTO;
import com.example.projectflow.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "*")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    // GET /api/notifications/user/{userId}
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificationDTO>> getNotifications(
            @PathVariable Integer userId) {

        List<NotificationDTO> notifications =
                notificationService.getNotifications(userId);
        return ResponseEntity.ok(notifications);
    }

    // GET /api/notifications/user/{userId}/non-lues
    @GetMapping("/user/{userId}/non-lues")
    public ResponseEntity<List<NotificationDTO>> getNotificationsNonLues(
            @PathVariable Integer userId) {

        List<NotificationDTO> notifications =
                notificationService.getNotificationsNonLues(userId);
        return ResponseEntity.ok(notifications);
    }

    // GET /api/notifications/user/{userId}/count
    @GetMapping("/user/{userId}/count")
    public ResponseEntity<Map<String, Long>> getCountNonLues(
            @PathVariable Integer userId) {

        long count = notificationService.compterNotificationsNonLues(userId);
        Map<String, Long> response = new HashMap<>();
        response.put("count", count);
        return ResponseEntity.ok(response);
    }

    // PUT /api/notifications/{id}/lire
    @PutMapping("/{id}/lire")
    public ResponseEntity<Void> marquerCommeLue(@PathVariable Integer id) {
        notificationService.marquerCommeLue(id);
        return ResponseEntity.ok().build();
    }

    // PUT /api/notifications/user/{userId}/lire-toutes
    @PutMapping("/user/{userId}/lire-toutes")
    public ResponseEntity<Void> marquerToutesCommeLues(@PathVariable Integer userId) {
        notificationService.marquerToutesCommeLues(userId);
        return ResponseEntity.ok().build();
    }

    // DELETE /api/notifications/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Integer id) {
        notificationService.deleteNotification(id);
        return ResponseEntity.noContent().build();
    }
}