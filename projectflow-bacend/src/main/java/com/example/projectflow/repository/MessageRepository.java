package com.example.projectflow.repository;

import com.example.projectflow.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {

    // Messages entre un étudiant et un professeur
    @Query("SELECT m FROM Message m WHERE " +
            "(m.etudiant.id = :etudiantId AND m.professeur.id = :profId) OR " +
            "(m.professeur.id = :profId AND m.etudiant.id = :etudiantId) " +
            "ORDER BY m.dateEnvoi ASC")
    List<Message> findConversation(@Param("etudiantId") Integer etudiantId,
                                   @Param("profId") Integer profId);
}