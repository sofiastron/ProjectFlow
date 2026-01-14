package com.example.projectflow.service;

import com.example.projectflow.entity.Message;
import com.example.projectflow.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepo;

    public List<Message> getMessagesNonLus(Long etudiantId) {
        return messageRepo.findByDestinataireIdAndLuFalse(etudiantId);
    }

    public int countMessagesNonLus(Long etudiantId) {
        return messageRepo.countByDestinataireIdAndLuFalse(etudiantId);
    }
}
