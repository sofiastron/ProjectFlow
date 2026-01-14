package com.example.projectflow.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.projectflow.entity.Message;
import com.example.projectflow.service.MessageService;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @GetMapping("/etudiant/{etudiantId}/non-lus")
    public List<Message> getMessagesNonLus(@PathVariable Long etudiantId) {
        return messageService.getMessagesNonLus(etudiantId);
    }
}
