package com.example.projectflow.repository;

import java.util.List; 

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.projectflow.entity.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByDestinataireIdAndLuFalse(Long etudiantId);

    int countByDestinataireIdAndLuFalse(Long etudiantId);
}
