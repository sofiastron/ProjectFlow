package com.example.projectflow.service;

import com.example.projectflow.model.CahierCharges;
import com.example.projectflow.repository.CahierChargesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CahierChargesService {

    @Autowired
    private CahierChargesRepository repository;

    @Autowired
    private PdfService pdfService;

    /**
     * Sauvegarder avec génération PDF
     */
    public CahierCharges saveWithPdf(CahierCharges cahier) {
        System.out.println("📄 Service: saveWithPdf");

        // Date automatique
        if (cahier.getDateCreation() == null) {
            cahier.setDateCreation(LocalDateTime.now());
        }

        // 1. Sauvegarder pour avoir ID
        CahierCharges saved = repository.save(cahier);
        System.out.println("💾 DB sauvegardé, ID: " + saved.getId());

        // 2. Générer PDF
        try {
            String pdfPath = pdfService.generatePdf(saved);
            System.out.println("🔄 PDF généré: " + pdfPath);

            // 3. Mettre à jour avec chemin PDF
            saved.setFichierPdf(pdfPath);
            saved = repository.save(saved);

            System.out.println("✅ PDF lié au cahier");

        } catch (Exception e) {
            System.err.println("⚠️ Erreur PDF: " + e.getMessage());
            // On garde l'enregistrement même sans PDF
        }

        return saved;
    }

    /**
     * Sauvegarder un cahier (sans génération PDF)
     */
    public CahierCharges save(CahierCharges cahier) {
        return repository.save(cahier);
    }

    /**
     * Trouver par ID
     */
    public Optional<CahierCharges> findById(Long id) {
        return repository.findById(id);
    }

    /**
     * Récupérer tous les cahiers
     */
    public List<CahierCharges> getAllCahiers() {
        List<CahierCharges> cahiers = repository.findAll();
        System.out.println("📋 Service: " + cahiers.size() + " cahiers trouvés");
        return cahiers;
    }

    /**
     * Récupérer un cahier par ID (version qui lance une exception si non trouvé)
     */
    public CahierCharges getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cahier non trouvé avec id: " + id));
    }

    /**
     * Supprimer un cahier
     */
    public void deleteCahier(Long id) {
        System.out.println("🗑️ Service: Suppression du cahier ID: " + id);

        // Vérifier d'abord que le cahier existe
        if (!repository.existsById(id)) {
            throw new RuntimeException("Cahier non trouvé avec id: " + id);
        }

        // Supprimer le fichier PDF associé si nécessaire
        Optional<CahierCharges> cahierOpt = repository.findById(id);
        cahierOpt.ifPresent(cahier -> {
            if (cahier.getFichierPdf() != null) {
                // Optionnel: Supprimer le fichier PDF du disque
                // File pdfFile = new File(cahier.getFichierPdf());
                // pdfFile.delete();
            }
        });

        // Supprimer de la base de données
        repository.deleteById(id);
        System.out.println("✅ Cahier ID: " + id + " supprimé");
    }

    /**
     * Valider un cahier
     */
    public CahierCharges validerCahier(Long id) {
        CahierCharges cahier = getById(id);

        // Si votre modèle a un champ statut, le mettre à jour
        if (cahier.getStatut() != null) {
            cahier.setStatut("validé");
        }

        return repository.save(cahier);
    }

    /**
     * Dévalider un cahier
     */
    public CahierCharges invaliderCahier(Long id) {
        CahierCharges cahier = getById(id);

        if (cahier.getStatut() != null) {
            cahier.setStatut("en_attente");
        }

        return repository.save(cahier);
    }
}