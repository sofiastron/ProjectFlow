package com.example.projectflow.service;

import com.example.projectflow.entity.Parametre;
import com.example.projectflow.repository.ParametreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ParametreService {
    
    private final ParametreRepository parametreRepository;
    
    /**
     * Obtenir tous les paramètres
     */
    public List<Parametre> getAllParametres() {
        return parametreRepository.findAllOrderedByTypeAndCle();
    }
    
    /**
     * Obtenir paramètres par type
     */
    public List<Parametre> getParametresByType(Parametre.TypeParametre type) {
        return parametreRepository.findByType(type);
    }
    
    /**
     * Obtenir un paramètre par clé
     */
    public Optional<Parametre> getParametreByCle(String cle) {
        return parametreRepository.findByCle(cle);
    }
    
    /**
     * Obtenir la valeur d'un paramètre
     */
    public String getValeur(String cle, String valeurParDefaut) {
        return parametreRepository.findValeurByCle(cle)
            .orElse(valeurParDefaut);
    }
    
    public Integer getValeurInt(String cle, Integer valeurParDefaut) {
        return parametreRepository.findValeurByCle(cle)
            .map(Integer::parseInt)
            .orElse(valeurParDefaut);
    }
    
    public Boolean getValeurBoolean(String cle, Boolean valeurParDefaut) {
        return parametreRepository.findValeurByCle(cle)
            .map(Boolean::parseBoolean)
            .orElse(valeurParDefaut);
    }
    
    /**
     * Créer ou mettre à jour un paramètre
     */
    @Transactional
    public Parametre saveParametre(Parametre parametre) {
        Optional<Parametre> existant = parametreRepository.findByCle(parametre.getCle());
        
        if (existant.isPresent()) {
            Parametre p = existant.get();
            p.setValeur(parametre.getValeur());
            p.setDescription(parametre.getDescription());
            p.setType(parametre.getType());
            return parametreRepository.save(p);
        }
        
        return parametreRepository.save(parametre);
    }
    
    /**
     * Mettre à jour la valeur d'un paramètre
     */
    @Transactional
    public Parametre updateValeur(String cle, String nouvelleValeur) {
        Parametre parametre = parametreRepository.findByCle(cle)
            .orElseThrow(() -> new RuntimeException("Paramètre non trouvé : " + cle));
        
        parametre.setValeur(nouvelleValeur);
        return parametreRepository.save(parametre);
    }
    
    /**
     * Supprimer un paramètre
     */
    @Transactional
    public void supprimerParametre(Long id) {
        parametreRepository.deleteById(id);
    }
    
    /**
     * Initialiser les paramètres par défaut
     */
    @Transactional
    public void initialiserParametresParDefaut() {
        creerSiNonExiste(
            Parametre.CAPACITE_MAX_ENCADRANT, 
            "5", 
            "Nombre maximum d'étudiants par encadrant",
            Parametre.TypeParametre.AFFECTATION
        );
        
        creerSiNonExiste(
            Parametre.DUREE_SOUTENANCE_MIN, 
            "60", 
            "Durée d'une soutenance en minutes",
            Parametre.TypeParametre.SOUTENANCE
        );
        
        creerSiNonExiste(
            Parametre.NOTIFICATION_AFFECTATION, 
            "true", 
            "Activer les notifications d'affectation",
            Parametre.TypeParametre.NOTIFICATION
        );
        
        creerSiNonExiste(
            Parametre.NOTIFICATION_SOUTENANCE, 
            "true", 
            "Activer les notifications de soutenance",
            Parametre.TypeParametre.NOTIFICATION
        );
        
        creerSiNonExiste(
            Parametre.DELAI_MIN_SOUTENANCE_JOURS, 
            "7", 
            "Délai minimum pour planifier une soutenance (en jours)",
            Parametre.TypeParametre.SOUTENANCE
        );
    }
    
    private void creerSiNonExiste(String cle, String valeur, String description, 
                                  Parametre.TypeParametre type) {
        if (!parametreRepository.existsByCle(cle)) {
            Parametre parametre = new Parametre();
            parametre.setCle(cle);
            parametre.setValeur(valeur);
            parametre.setDescription(description);
            parametre.setType(type);
            parametreRepository.save(parametre);
        }
    }
}