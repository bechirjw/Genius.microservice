package com.genius.events.control;

import com.genius.events.entity.Evenements;
import com.genius.events.entity.StatutEvenement;
import com.genius.events.repository.ParticipationsRepository;
import com.genius.events.service.IEvenementsService;

import com.genius.events.service.PdfExportService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//@CrossOrigin(origins = "http://localhost:4200")
@RestController
@AllArgsConstructor
@RequestMapping("/evenements")
public class EvenementsRestController {

    IEvenementsService evenementsService;
    private final ParticipationsRepository participationRepository;
    private final PdfExportService pdfExportService;
    @GetMapping("/retrieve-all-evenements")
    public List<Evenements> getEvenements() {
        return evenementsService.retrieveAllEvenements()
                .stream()
                .filter(e -> e.getStatut() == StatutEvenement.APPROUVE)
                .collect(Collectors.toList());
    }

    @GetMapping("/retrieve-evenement/{evenement-id}")
    public Evenements retrieveEvenement(@PathVariable("evenement-id") Long evenementId) {
        return evenementsService.retrieveEvenement(evenementId);
    }

    @PostMapping(value = "/add-evenement", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Evenements> addEvenement(
            @RequestParam(value = "image", required = false) MultipartFile imageFile,
            @RequestParam("titre") String titre,
            @RequestParam("description") String description,
            @RequestParam("dateDebut") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateDebut,
            @RequestParam("dateFin") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateFin,
            @RequestParam("lieu") String lieu,
            @RequestParam("categorie") String categorie,
            @RequestParam("nbMaxParticipants") Integer nbMaxParticipants
    ) {
        try {
            String imagePath = null;
            if (imageFile != null && !imageFile.isEmpty()) {
                String uploadDir = "uploads/";
                String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
                Path filePath = Paths.get(uploadDir + fileName);
                Files.createDirectories(filePath.getParent());
                Files.write(filePath, imageFile.getBytes());
                imagePath = uploadDir + fileName;
            }

            Evenements evenement = new Evenements();
            evenement.setTitre(titre);
            evenement.setDescription(description);
            evenement.setDateDebut(dateDebut);
            evenement.setDateFin(dateFin);
            evenement.setLieu(lieu);
            evenement.setCategorie(categorie);
            evenement.setNbMaxParticipants(nbMaxParticipants);
            evenement.setStatut(StatutEvenement.NON_TRAITE); // 👈 Défaut
            evenement.setImage(imagePath);

            Evenements saved = evenementsService.addEvenement(evenement);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping(value = "/modify-evenement", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Evenements> updateEvenement(
            @RequestParam("id") Long id,
            @RequestParam("titre") String titre,
            @RequestParam("description") String description,
            @RequestParam("dateDebut") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateDebut,
            @RequestParam("dateFin") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateFin,
            @RequestParam("lieu") String lieu,
            @RequestParam("categorie") String categorie,
            @RequestParam("nbMaxParticipants") Integer nbMaxParticipants,
            @RequestParam(value = "image", required = false) MultipartFile imageFile
    ) {
        try {
            Evenements evenement = evenementsService.retrieveEvenement(id);
            if (evenement == null) {
                return ResponseEntity.notFound().build();
            }

            evenement.setTitre(titre);
            evenement.setDescription(description);
            evenement.setDateDebut(dateDebut);
            evenement.setDateFin(dateFin);
            evenement.setLieu(lieu);
            evenement.setCategorie(categorie);
            evenement.setNbMaxParticipants(nbMaxParticipants);

            if (imageFile != null && !imageFile.isEmpty()) {
                String uploadDir = "uploads/";
                String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
                Path filePath = Paths.get(uploadDir + fileName);
                Files.createDirectories(filePath.getParent());
                Files.write(filePath, imageFile.getBytes());
                evenement.setImage(uploadDir + fileName);
            }

            Evenements updated = evenementsService.modifyEvenement(evenement);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/remove-evenement/{evenement-id}")
    public void removeEvenement(@PathVariable("evenement-id") Long evenementId) {
        evenementsService.removeEvenement(evenementId);
    }

    @PostMapping("/upload-image")
    public ResponseEntity<String> uploadImage(@RequestParam("image") MultipartFile imageFile) {
        try {
            String uploadDir = "uploads/";
            String fileName = imageFile.getOriginalFilename();
            File dir = new File(uploadDir);
            if (!dir.exists()) dir.mkdirs();

            Path filePath = Paths.get(uploadDir + fileName);
            Files.write(filePath, imageFile.getBytes());
            return ResponseEntity.ok(filePath.toString());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de l'upload");
        }
    }

    @GetMapping("/count-participants/{idEvent}")
    public Long countParticipants(@PathVariable Long idEvent) {
        return participationRepository.countByEvenementId(idEvent);
    }

    // ✅ APPROUVER UN ÉVÉNEMENT (utilisé par l’admin)
    @PutMapping("/approve-event/{id}")
    public ResponseEntity<Evenements> approveEvent(@PathVariable Long id) {
        Evenements evenement = evenementsService.retrieveEvenement(id);
        if (evenement == null) {
            return ResponseEntity.notFound().build();
        }

        evenement.setStatut(StatutEvenement.APPROUVE);
        return ResponseEntity.ok(evenementsService.modifyEvenement(evenement));
    }

    @GetMapping("/non-traite")
    public List<Evenements> getEvenementsNonTraites() {
        return evenementsService.getEvenementsByStatut(StatutEvenement.NON_TRAITE);
    }
    @PutMapping("/changer-statut/{id}")
    public ResponseEntity<Evenements> changerStatut(
            @PathVariable Long id,
            @RequestParam StatutEvenement statut) {

        Evenements evenement = evenementsService.retrieveEvenement(id);
        if (evenement == null) {
            return ResponseEntity.notFound().build();
        }

        evenement.setStatut(statut);
        Evenements updated = evenementsService.modifyEvenement(evenement);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/approuves")
    public List<Evenements> getEvenementsApprouves() {
        return evenementsService.getEvenementsByStatut(StatutEvenement.APPROUVE);
    }
    @GetMapping("/stats-globale")
    public Map<String, Object> getStatsGlobale() {
        List<Evenements> approuves = evenementsService.getEvenementsByStatut(StatutEvenement.APPROUVE);

        long totalParticipants = approuves.stream()
                .mapToLong(e -> participationRepository.countByEvenementId(e.getId()))
                .sum();

        int totalMax = approuves.stream()
                .mapToInt(Evenements::getNbMaxParticipants)
                .sum();

        double taux = totalMax == 0 ? 0 : (totalParticipants * 100.0) / totalMax;

        List<Map<String, Object>> parEvenement = approuves.stream().map(e -> {
            long count = participationRepository.countByEvenementId(e.getId());
            double tauxEv = e.getNbMaxParticipants() == 0 ? 0 : (count * 100.0) / e.getNbMaxParticipants();

            Map<String, Object> stat = new HashMap<>();
            stat.put("titre", e.getTitre());
            stat.put("tauxParticipation", tauxEv);
            return stat;
        }).toList();

        Map<String, Object> result = new HashMap<>();
        result.put("totalEvenements", approuves.size());
        result.put("totalParticipants", totalParticipants);
        result.put("tauxGlobalParticipation", (int) taux);
        result.put("parEvenement", parEvenement);

        return result;
    }
    @GetMapping("/export/pdf")
    public void exportToPdf(HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=evenements.pdf";
        response.setHeader(headerKey, headerValue);

        List<Evenements> evenements = evenementsService.retrieveAllEvenements();
        pdfExportService.exportDeuxTables(evenements, response);
    }
}
