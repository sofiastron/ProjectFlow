package com.example.projectflow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    
    private Boolean success;
    private String message;
    private T data;
    private LocalDateTime timestamp;
    private String error;
    
    // Constructeur pour succès
    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(true, message, data, LocalDateTime.now(), null);
    }
    
    public static <T> ApiResponse<T> success(T data) {
        return success(data, "Opération réussie");
    }
    
    // Constructeur pour erreur
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, message, null, LocalDateTime.now(), message);
    }
    
    public static <T> ApiResponse<T> error(String message, String errorDetails) {
        return new ApiResponse<>(false, message, null, LocalDateTime.now(), errorDetails);
    }
    
    // Constructeur pour validation
    public static <T> ApiResponse<T> validation(String message) {
        return new ApiResponse<>(false, message, null, LocalDateTime.now(), "Erreur de validation");
    }
}