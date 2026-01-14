package com.example.projectflow.controller;

import com.example.projectflow.service.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = {"http://localhost:5173", "http://127.0.0.1:5173"})
public class RecommendationController {

    @Autowired
    private RecommendationService recommendationService;


}
