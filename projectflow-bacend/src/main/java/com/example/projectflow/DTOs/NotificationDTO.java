package com.example.projectflow.DTOs;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDTO {
    private Integer id;
    private String message;
    private Date date;
    private Boolean vu;
    private Integer tacheId;
    private String tacheTitre;
    private String etudiantNom;
}