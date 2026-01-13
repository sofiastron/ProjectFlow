package com.example.projectflow.DTOs;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KanbanDTO {
    private Map<String, List<TacheDTO>> colonnes;
}