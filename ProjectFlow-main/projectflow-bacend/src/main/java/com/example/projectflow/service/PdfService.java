package com.example.projectflow.service;

import com.example.projectflow.model.CahierCharges;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class PdfService {

    @Value("${pdf.directory:pdfs/}")
    private String pdfDirectory;

    // Couleurs professionnelles
    private static final BaseColor PRIMARY_COLOR = new BaseColor(0, 51, 102);      // Bleu foncé professionnel
    private static final BaseColor SECONDARY_COLOR = new BaseColor(0, 102, 153);   // Bleu medium
    private static final BaseColor ACCENT_COLOR = new BaseColor(220, 53, 69);      // Rouge pour accents
    private static final BaseColor DARK_COLOR = new BaseColor(33, 37, 41);         // Noir professionnel
    private static final BaseColor LIGHT_COLOR = new BaseColor(248, 249, 250);     // Gris très clair
    private static final BaseColor TABLE_HEADER_COLOR = new BaseColor(233, 236, 239); // Gris clair pour tableau

    public String generatePdf(CahierCharges cahier) {
        try {
            // Créer le dossier si nécessaire
            Path pdfPath = Paths.get(pdfDirectory);
            if (!Files.exists(pdfPath)) {
                Files.createDirectories(pdfPath);
                System.out.println("📁 Dossier PDF créé: " + pdfPath.toAbsolutePath());
            }

            // Nom de fichier
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String safeFileName = cahier.getSujetProjet()
                    .replaceAll("[^a-zA-Z0-9.-]", "_")
                    .substring(0, Math.min(30, cahier.getSujetProjet().length()));

            String fileName = String.format("Cahier_Charges_PRO_%d_%s_%s.pdf",
                    cahier.getId(), safeFileName, timestamp);

            String fullPath = pdfDirectory + fileName;

            System.out.println("🎨 Génération PDF professionnel...");

            // Créer le document
            Document document = new Document(PageSize.A4, 36, 36, 90, 60); // Marges ajustées pour header/footer
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(fullPath));

            // Ajouter événement pour header/footer professionnel
            writer.setPageEvent(new ProfessionalHeaderFooter(cahier));

            document.open();

            // Ajouter le contenu professionnel
            addProfessionalContent(document, cahier);

            document.close();

            System.out.println("✅ PDF professionnel généré: " + fullPath);
            return fullPath;

        } catch (Exception e) {
            System.err.println("❌ Erreur génération PDF professionnel: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private void addProfessionalContent(Document document, CahierCharges cahier) throws DocumentException {
        // Nom du projet en GRAND avec style Helvetica
        Font projectNameFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 26);
        projectNameFont.setColor(PRIMARY_COLOR);

        // Mettre le nom du projet en majuscules pour un effet plus professionnel
        String projectName = cahier.getSujetProjet().toUpperCase();
        Paragraph projectTitle = new Paragraph(projectName, projectNameFont);
        projectTitle.setAlignment(Element.ALIGN_CENTER);
        projectTitle.setSpacingAfter(10);
        document.add(projectTitle);

        // Sous-titre "Cahier de Charges"
        Font subtitleFont = FontFactory.getFont(FontFactory.HELVETICA, 16);
        subtitleFont.setColor(SECONDARY_COLOR);
        Paragraph subtitle = new Paragraph("Cahier de Charges", subtitleFont);
        subtitle.setAlignment(Element.ALIGN_CENTER);
        subtitle.setSpacingAfter(5);
        document.add(subtitle);

        // Système de Salle en plus petit
        Font systemFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
        systemFont.setColor(new BaseColor(100, 100, 100));
        Paragraph systemText = new Paragraph("SYSTÈME DE SALLE", systemFont);
        systemText.setAlignment(Element.ALIGN_CENTER);
        systemText.setSpacingAfter(25);
        document.add(systemText);

        // Référence du document
        Font refFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
        refFont.setColor(DARK_COLOR);
        Paragraph ref = new Paragraph("Réf: CC-PRO-" + cahier.getId(), refFont);
        ref.setAlignment(Element.ALIGN_CENTER);
        ref.setSpacingAfter(30);
        document.add(ref);

        // Informations principales dans un tableau professionnel
        document.add(createProfessionalInfoTable(cahier));

        document.add(new Paragraph("\n"));

        // Sections professionnelles
        addProfessionalSection(document, "1. CONTEXTE ET PROBLÉMATIQUE", cahier.getSujetProblematique());

        if (cahier.getBesoinsFonctionnels() != null && !cahier.getBesoinsFonctionnels().isEmpty()) {
            addProfessionalSection(document, "2. BESOINS FONCTIONNELS", cahier.getBesoinsFonctionnels());
        }

        if (cahier.getBesoinsNonFonctionnels() != null && !cahier.getBesoinsNonFonctionnels().isEmpty()) {
            addProfessionalSection(document, "3. BESOINS NON-FONCTIONNELS", cahier.getBesoinsNonFonctionnels());
        }

        if (cahier.getTechnologiesProposees() != null && !cahier.getTechnologiesProposees().isEmpty()) {
            addProfessionalSection(document, "4. TECHNOLOGIES ET OUTILS", cahier.getTechnologiesProposees());
        }

        // Section contraintes et délais
        addConstraintSection(document);

        // Section de validation
        addValidationSection(document);
    }

    private PdfPTable createProfessionalInfoTable(CahierCharges cahier) throws DocumentException {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(30f);

        // Style des cellules d'en-tête
        PdfPCell headerCell = new PdfPCell();
        headerCell.setBackgroundColor(TABLE_HEADER_COLOR);
        headerCell.setBorderWidth(1);
        headerCell.setBorderColor(DARK_COLOR);
        headerCell.setPadding(10);
        headerCell.setHorizontalAlignment(Element.ALIGN_LEFT);

        // Style des cellules de contenu
        PdfPCell contentCell = new PdfPCell();
        contentCell.setBorderWidth(1);
        contentCell.setBorderColor(LIGHT_COLOR);
        contentCell.setPadding(10);
        contentCell.setBackgroundColor(BaseColor.WHITE);

        // Titre du projet (version courte)
        String shortProjectName = cahier.getSujetProjet();
        if (shortProjectName.length() > 40) {
            shortProjectName = shortProjectName.substring(0, 37) + "...";
        }
        addProfessionalTableRow(table, "INTITULÉ DU PROJET", shortProjectName.toUpperCase(), headerCell, contentCell);

        // Référence interne
        addProfessionalTableRow(table, "RÉFÉRENCE INTERNE", "PROJ-" + cahier.getId(), headerCell, contentCell);

        // Date
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String dateFormatted = cahier.getDateCreation().format(formatter);
        addProfessionalTableRow(table, "DATE DE CRÉATION", dateFormatted, headerCell, contentCell);

        // Version
        addProfessionalTableRow(table, "VERSION DU DOCUMENT", "1.0", headerCell, contentCell);

        // Statut
        addProfessionalTableRow(table, "STATUT", "EN RÉDACTION", headerCell, contentCell);

        // Porteur du projet
        addProfessionalTableRow(table, "PORTEUR DU PROJET", "DIRECTION TECHNIQUE", headerCell, contentCell);

        return table;
    }

    private void addProfessionalTableRow(PdfPTable table, String label, String content,
                                         PdfPCell headerCell, PdfPCell contentCell) {
        // Cellule label
        Font labelFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9);
        labelFont.setColor(DARK_COLOR);
        Paragraph labelPara = new Paragraph(label, labelFont);

        PdfPCell labelCell = new PdfPCell(headerCell);
        labelCell.addElement(labelPara);
        table.addCell(labelCell);

        // Cellule contenu
        Font contentFont = FontFactory.getFont(FontFactory.HELVETICA, 9);
        contentFont.setColor(DARK_COLOR);
        Paragraph contentPara = new Paragraph(content, contentFont);

        PdfPCell dataCell = new PdfPCell(contentCell);
        dataCell.addElement(contentPara);
        table.addCell(dataCell);
    }

    private void addProfessionalSection(Document document, String title, String content) throws DocumentException {
        // Titre de section avec numérotation - Style Helvetica
        Font sectionTitleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
        sectionTitleFont.setColor(PRIMARY_COLOR);

        Paragraph sectionTitle = new Paragraph(title, sectionTitleFont);
        sectionTitle.setSpacingBefore(20);
        sectionTitle.setSpacingAfter(8);
        document.add(sectionTitle);

        // Ligne séparatrice fine
        Paragraph line = new Paragraph("________________________________________________________________");
        line.setSpacingAfter(12);
        document.add(line);

        // Contenu justifié - Style Helvetica
        Font contentFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
        contentFont.setColor(DARK_COLOR);

        Paragraph contentPara = new Paragraph(content, contentFont);
        contentPara.setAlignment(Element.ALIGN_JUSTIFIED);
        contentPara.setSpacingAfter(25);
        contentPara.setLeading(14); // Interligne
        document.add(contentPara);
    }

    private void addConstraintSection(Document document) throws DocumentException {
        Font sectionTitleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
        sectionTitleFont.setColor(PRIMARY_COLOR);

        Paragraph sectionTitle = new Paragraph("5. CONTRAINTES ET DÉLAIS", sectionTitleFont);
        sectionTitle.setSpacingBefore(20);
        sectionTitle.setSpacingAfter(8);
        document.add(sectionTitle);

        Paragraph line = new Paragraph("________________________________________________________________");
        line.setSpacingAfter(12);
        document.add(line);

        // Tableau des contraintes
        PdfPTable constraintsTable = new PdfPTable(2);
        constraintsTable.setWidthPercentage(100);
        constraintsTable.setSpacingAfter(20);

        // Style
        PdfPCell headerCell = new PdfPCell();
        headerCell.setBackgroundColor(TABLE_HEADER_COLOR);
        headerCell.setBorderWidth(1);
        headerCell.setBorderColor(DARK_COLOR);
        headerCell.setPadding(8);

        PdfPCell contentCell = new PdfPCell();
        contentCell.setBorderWidth(1);
        contentCell.setBorderColor(LIGHT_COLOR);
        contentCell.setPadding(8);

        // Contraintes
        String[][] constraints = {
                {"Délai de réalisation", "À définir"},
                {"Budget alloué", "À définir"},
                {"Équipe projet", "À constituer"},
                {"Environnements", "Développement, Test, Production"},
                {"Sécurité", "Normes en vigueur"},
                {"Maintenabilité", "Documentation technique requise"}
        };

        Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 9);
        cellFont.setColor(DARK_COLOR);

        for (String[] constraint : constraints) {
            Paragraph label = new Paragraph(constraint[0], cellFont);
            PdfPCell labelCell = new PdfPCell(headerCell);
            labelCell.addElement(label);
            constraintsTable.addCell(labelCell);

            Paragraph value = new Paragraph(constraint[1], cellFont);
            PdfPCell valueCell = new PdfPCell(contentCell);
            valueCell.addElement(value);
            constraintsTable.addCell(valueCell);
        }

        document.add(constraintsTable);
    }

    private void addValidationSection(Document document) throws DocumentException {
        document.add(new Paragraph("\n\n\n"));

        Font validationFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);
        validationFont.setColor(PRIMARY_COLOR);

        Paragraph validationTitle = new Paragraph("VALIDATION DU DOCUMENT", validationFont);
        validationTitle.setAlignment(Element.ALIGN_CENTER);
        validationTitle.setSpacingAfter(20);
        document.add(validationTitle);

        // Tableau de validation
        PdfPTable validationTable = new PdfPTable(3);
        validationTable.setWidthPercentage(100);
        validationTable.setWidths(new float[]{1, 1, 1});

        String[] roles = {"RÉDACTEUR", "VALIDATEUR TECHNIQUE", "CHEF DE PROJET"};

        for (String role : roles) {
            PdfPCell cell = new PdfPCell();
            cell.setBorderWidth(1);
            cell.setBorderColor(LIGHT_COLOR);
            cell.setPadding(15);
            cell.setMinimumHeight(80);

            Font roleFont = FontFactory.getFont(FontFactory.HELVETICA, 8);
            roleFont.setColor(DARK_COLOR);

            Paragraph rolePara = new Paragraph(role + "\n\n\n\n", roleFont);
            rolePara.setAlignment(Element.ALIGN_CENTER);

            Font nameFont = FontFactory.getFont(FontFactory.HELVETICA, 9);
            nameFont.setColor(ACCENT_COLOR);

            Paragraph namePara = new Paragraph("__________________________\nNom et signature", nameFont);
            namePara.setAlignment(Element.ALIGN_CENTER);

            cell.addElement(rolePara);
            cell.addElement(namePara);
            validationTable.addCell(cell);
        }

        document.add(validationTable);

        // Pied de document
        document.add(new Paragraph("\n\n"));

        Font footerFont = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 8);
        footerFont.setColor(new BaseColor(100, 100, 100));

        Paragraph footer = new Paragraph(
                "Document confidentiel - Propriété de ProjectFlow Solutions - " +
                        LocalDateTime.now().getYear(),
                footerFont
        );
        footer.setAlignment(Element.ALIGN_CENTER);
        document.add(footer);
    }

    /**
     * Classe pour header/footer professionnel
     */
    class ProfessionalHeaderFooter extends PdfPageEventHelper {
        private CahierCharges cahier;
        private PdfTemplate totalPages;

        public ProfessionalHeaderFooter(CahierCharges cahier) {
            this.cahier = cahier;
        }

        @Override
        public void onOpenDocument(PdfWriter writer, Document document) {
            totalPages = writer.getDirectContent().createTemplate(30, 16);
        }

        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            try {
                PdfContentByte cb = writer.getDirectContent();
                float headerY = document.top() + 10;
                float footerY = document.bottom() - 20;

                // HEADER PROFESSIONNEL
                // Fond du header
                cb.setColorFill(PRIMARY_COLOR);
                cb.rectangle(36, headerY, document.getPageSize().getWidth() - 72, 40);
                cb.fill();

                // Logo/Nom de l'entreprise - Helvetica Bold
                Font companyFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
                companyFont.setColor(BaseColor.WHITE);

                ColumnText.showTextAligned(cb, Element.ALIGN_LEFT,
                        new Phrase("PROJECTFLOW SOLUTIONS", companyFont),
                        40, headerY + 22, 0);

                // Nom du projet dans l'en-tête - Helvetica
                Font projectFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
                projectFont.setColor(LIGHT_COLOR);

                String projectName = cahier.getSujetProjet();
                if (projectName.length() > 50) {
                    projectName = projectName.substring(0, 47) + "...";
                }

                ColumnText.showTextAligned(cb, Element.ALIGN_LEFT,
                        new Phrase("Projet: " + projectName, projectFont),
                        40, headerY + 8, 0);

                // Référence du document - Helvetica
                Font refFont = FontFactory.getFont(FontFactory.HELVETICA, 8);
                refFont.setColor(LIGHT_COLOR);

                ColumnText.showTextAligned(cb, Element.ALIGN_RIGHT,
                        new Phrase("CC-PRO-" + cahier.getId(), refFont),
                        document.getPageSize().getWidth() - 40, headerY + 22, 0);

                // Date - Helvetica
                String dateText = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                ColumnText.showTextAligned(cb, Element.ALIGN_RIGHT,
                        new Phrase("Date: " + dateText, refFont),
                        document.getPageSize().getWidth() - 40, headerY + 8, 0);

                // FOOTER PROFESSIONNEL
                // Ligne de séparation
                cb.setColorStroke(new BaseColor(200, 200, 200));
                cb.setLineWidth(0.5f);
                cb.moveTo(36, footerY + 12);
                cb.lineTo(document.getPageSize().getWidth() - 36, footerY + 12);
                cb.stroke();

                // Numéro de page - Helvetica
                Font pageFont = FontFactory.getFont(FontFactory.HELVETICA, 8);
                pageFont.setColor(DARK_COLOR);

                String pageText = "Page " + writer.getPageNumber() + " sur ";
                float pageTextWidth = FontFactory.getFont(FontFactory.HELVETICA, 8).getBaseFont().getWidthPoint(pageText, 8);

                ColumnText.showTextAligned(cb, Element.ALIGN_LEFT,
                        new Phrase(pageText, pageFont),
                        40, footerY, 0);

                // Total pages
                cb.addTemplate(totalPages, 40 + pageTextWidth, footerY);

                // Confidentialité - Helvetica
                Font confidentialFont = FontFactory.getFont(FontFactory.HELVETICA, 8);
                confidentialFont.setColor(ACCENT_COLOR);

                ColumnText.showTextAligned(cb, Element.ALIGN_CENTER,
                        new Phrase("DOCUMENT CONFIDENTIEL", confidentialFont),
                        (document.right() + document.left()) / 2, footerY, 0);

                // Version - Helvetica
                ColumnText.showTextAligned(cb, Element.ALIGN_RIGHT,
                        new Phrase("Version 1.0", pageFont),
                        document.getPageSize().getWidth() - 40, footerY, 0);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onCloseDocument(PdfWriter writer, Document document) {
            // Ajouter le nombre total de pages
            Font pageFont = FontFactory.getFont(FontFactory.HELVETICA, 8);
            pageFont.setColor(DARK_COLOR);

            ColumnText.showTextAligned(totalPages, Element.ALIGN_LEFT,
                    new Phrase(String.valueOf(writer.getPageNumber()), pageFont),
                    0, 0, 0);
        }
    }
}