package com.example.projectflow.repository;

import com.example.projectflow.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {

    // Trouver les notifications non lues d'un utilisateur
    List<Notification> findByUserIdAndVuFalseOrderByDateDesc(Integer userId);

    // Toutes les notifications d'un utilisateur
    List<Notification> findByUserIdOrderByDateDesc(Integer userId);

    // Compter les notifications non lues
    long countByUserIdAndVuFalse(Integer userId);
}