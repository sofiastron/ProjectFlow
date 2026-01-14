package com.example.projectflow.repository;

import com.example.projectflow.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    int countByUserIdAndVuFalse(Long userId);
    List<Notification> findByUserIdAndVuFalse(Long userId);
}
