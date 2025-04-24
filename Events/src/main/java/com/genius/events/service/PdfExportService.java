package com.genius.events.service;

import com.genius.events.entity.Evenements;
import com.genius.events.entity.StatutEvenement;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Stream;

@Service
public class PdfExportService {

    public void exportDeuxTables(List<Evenements> evenements, HttpServletResponse response) throws IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, Color.BLACK);
        Font sectionFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, Color.BLUE);

        // Titre principal
        Paragraph title = new Paragraph("Liste des Événements", titleFont);
        title.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(title);
        document.add(new Paragraph(" "));

        // Séparer les événements
        List<Evenements> approuves = evenements.stream()
                .filter(e -> e.getStatut() == StatutEvenement.APPROUVE)
                .toList();

        List<Evenements> nonTraites = evenements.stream()
                .filter(e -> e.getStatut() == StatutEvenement.NON_TRAITE)
                .toList();

        // Section Approuvés
        document.add(new Paragraph("🟢 Événements Approuvés", sectionFont));
        document.add(new Paragraph(" "));
        PdfPTable tableApprouves = createTable(evenements);
        remplirTable(tableApprouves, approuves);
        document.add(tableApprouves);
        document.add(new Paragraph(" "));

        // Section Non Traités
        document.add(new Paragraph("🕓 Événements Non Traités", sectionFont));
        document.add(new Paragraph(" "));
        PdfPTable tableNonTraites = createTable(evenements);
        remplirTable(tableNonTraites, nonTraites);
        document.add(tableNonTraites);

        document.close();
    }

    private PdfPTable createTable(List<Evenements> evenements) {
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setWidths(new int[]{3, 2, 5, 2});
        addTableHeader(table);
        return table;
    }

    private void addTableHeader(PdfPTable table) {
        Stream.of("Titre", "Date", "Description", "Statut").forEach(headerTitle -> {
            PdfPCell header = new PdfPCell();
            header.setBackgroundColor(Color.LIGHT_GRAY);
            header.setPhrase(new Phrase(headerTitle));
            table.addCell(header);
        });
    }

    private void remplirTable(PdfPTable table, List<Evenements> evenements) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        for (Evenements e : evenements) {
            table.addCell(e.getTitre());
            String dateStr = e.getDateDebut() != null ? formatter.format(e.getDateDebut().toLocalDate()) : "-";
            table.addCell(dateStr);
            table.addCell(e.getDescription());
            table.addCell(e.getStatut().name());
        }
    }
}
