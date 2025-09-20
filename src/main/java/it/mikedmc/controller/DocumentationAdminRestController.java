package it.mikedmc.controller;

import it.mikedmc.enums.DocumentationContentType;
import it.mikedmc.model.Documentation;
import it.mikedmc.model.DocumentationContent;
import it.mikedmc.model.dto.ContentDto;
import it.mikedmc.model.dto.ContentPositionDto;
import it.mikedmc.repository.DocumentationContentRepository;
import it.mikedmc.repository.DocumentationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.text.AbstractDocument;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@RestController
@RequestMapping("/api/admin/documentation")
public class DocumentationAdminRestController {

    @Autowired
    private DocumentationRepository documentationRepository;

    @Autowired
    private DocumentationContentRepository documentationContentRepository;

    @PutMapping("/edit/{id}")
    public ResponseEntity<?> updateDocumentation(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        Documentation doc = documentationRepository.findById(id).orElseThrow();

        doc.setName((String) updates.get("name"));
        doc.setDescription((String) updates.get("description"));
        doc.setPassword((String) updates.get("password"));
        doc.setCode((String) updates.get("code"));
        doc.setActive(Boolean.TRUE.equals(updates.get("isActive")));
        doc.setColorCode((String) updates.get("colorCode"));

        ZoneId zoneId = ZoneId.of("Europe/Rome");
        doc.setLastModifiedDate(LocalDateTime.now(zoneId));

        // Gestione automatica isPublic
        String password = (String) updates.get("password");
        if (password != null && !password.trim().isEmpty()) {
            doc.setPublic(false);
        } else {
            doc.setPublic(true);
        }

        documentationRepository.save(doc);
        return ResponseEntity.ok().build();
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateContent(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        DocumentationContent content = documentationContentRepository.findById(id).orElse(null);
        if (content == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Content not found");
        }

        if (updates.containsKey("type")) {
            try {
                DocumentationContentType type = DocumentationContentType.valueOf((String) updates.get("type"));
                content.setType(type);
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body("Invalid content type");
            }
        }

        content.setData((String) updates.getOrDefault("data", content.getData()));
        content.setOption1((String) updates.getOrDefault("option1", content.getOption1()));
        content.setOption2((String) updates.getOrDefault("option2", content.getOption2()));
        content.setOption3((String) updates.getOrDefault("option3", content.getOption3()));

        if (updates.containsKey("position")) {
            try {
                Long position = Long.parseLong(updates.get("position").toString());
                content.setPosition(position);
            } catch (NumberFormatException e) {
                return ResponseEntity.badRequest().body("Invalid position format");
            }
        }

        documentationContentRepository.save(content);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/content/swap")
    public ResponseEntity<?> swapContentPositions(@RequestBody List<ContentPositionDto> list) {
        for (ContentPositionDto dto : list) {
            documentationContentRepository.findById(dto.getId()).ifPresent(content -> {
                content.setPosition(dto.getPosition());
                documentationContentRepository.save(content);
            });
        }
        return ResponseEntity.ok().build();
    }

    @PutMapping("/content/{id}")
    public ResponseEntity<?> updateContent(@PathVariable Long id, @RequestBody DocumentationContent updatedContent) {

        System.out.println("updateContent " + updatedContent.toString());
        return documentationContentRepository.findById(id).map(existingContent -> {
            existingContent.setData(updatedContent.getData());
            existingContent.setOption1(updatedContent.getOption1());
            existingContent.setOption2(updatedContent.getOption2());
            existingContent.setOption3(updatedContent.getOption3());
            existingContent.setType(updatedContent.getType());
            existingContent.setPosition(updatedContent.getPosition());

            documentationContentRepository.save(existingContent);
            return ResponseEntity.ok().build();
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/contents/{id}")
    public ResponseEntity<?> deleteContent(@PathVariable Long id) {
        Optional<DocumentationContent> optionalContent = documentationContentRepository.findById(id);
        if (optionalContent.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        DocumentationContent content = optionalContent.get();

        // Supponendo che il tipo sia una enum o String chiamata getType()
        if ("TABLE".equalsIgnoreCase(content.getType().toString())) {
            String csvFilePath = content.getData();
            if (csvFilePath != null && !csvFilePath.isBlank()) {
                Path csvDirectory = Paths.get("website-data", "uploaded-csv");
                Path fileToDelete = csvDirectory.resolve(csvFilePath).normalize();

                try {
                    // Sicurezza: controlla che il file sia dentro la cartella csvDirectory
                    if (fileToDelete.startsWith(csvDirectory) && Files.exists(fileToDelete)) {
                        Files.delete(fileToDelete);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body("Errore durante la cancellazione del file CSV.");
                }
            }
        }

        documentationContentRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/save")
    public ResponseEntity<Map<String, Object>> saveContent(@RequestBody ContentDto dto) {
        DocumentationContent content = new DocumentationContent();
        content.setType(DocumentationContentType.valueOf(dto.getType()));
        content.setData(dto.getData());
        content.setOption1(dto.getOption1());
        content.setOption2(dto.getOption2());
        content.setOption3(dto.getOption3());
        content.setPosition(dto.getPosition());

        Documentation doc = documentationRepository.findById(dto.getDocumentationId())
                .orElseThrow(() -> new RuntimeException("Documentation not found"));
        content.setDocumentation(doc);

        DocumentationContent saved = documentationContentRepository.save(content);

        Map<String, Object> response = new HashMap<>();
        response.put("id", saved.getId());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/upload-csv/{id}")
    public ResponseEntity<String> uploadCsv(@PathVariable Long id,
                                            @RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty() || !file.getOriginalFilename().endsWith(".csv")) {
                return ResponseEntity.badRequest().body("File non valido");
            }

            // Path personalizzato
            Path csvDirectory = Paths.get("website-data", "uploaded-csv");

            // Crea le cartelle se non esistono
            if (Files.notExists(csvDirectory)) {
                Files.createDirectories(csvDirectory);
            }

            // Salvataggio con nome UUID per evitare conflitti
            String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = csvDirectory.resolve(filename);

            Files.write(filePath, file.getBytes());

            // Aggiorna il contenuto nel database
            Optional<DocumentationContent> optional = documentationContentRepository.findById(id);
            if (optional.isPresent()) {
                DocumentationContent content = optional.get();

                if (content.getType().equals(DocumentationContentType.TABLE)) {
                    String csvFilePath = content.getData();
                    if (csvFilePath != null && !csvFilePath.isBlank()) {
                        Path fileToDelete = csvDirectory.resolve(csvFilePath).normalize();

                        try {
                            // Sicurezza: controlla che il file sia dentro la cartella csvDirectory
                            if (fileToDelete.startsWith(csvDirectory) && Files.exists(fileToDelete)) {
                                Files.delete(fileToDelete);
                            }
                        } catch (IOException e) {

                        }
                    }
                }

                content.setType(DocumentationContentType.TABLE); // enum
                content.setData(filename); // salva path relativo
                documentationContentRepository.save(content);
            }

            return ResponseEntity.ok(filename);

        } catch (IOException e) {
            return ResponseEntity.status(500).body("Errore interno: " + e.getMessage());
        }
    }

    @PostMapping("/create")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> saveDocumentation(@RequestBody Documentation documentation) {
        ZoneId zoneId = ZoneId.of("Europe/Rome");
        documentation.setLastModifiedDate(LocalDateTime.now(zoneId));

        // Logica per determinare se è pubblica
        String password = documentation.getPassword();
        if (password != null && !password.trim().isEmpty()) {
            documentation.setPublic(false);
        } else {
            documentation.setPublic(true);
        }

        Documentation saved = documentationRepository.save(documentation);

        Map<String, Object> response = new HashMap<>();
        response.put("id", saved.getId());

        return ResponseEntity.ok(response);
    }

    @PutMapping("/toggle/{id}")
    public ResponseEntity<?> toggleActive(@PathVariable Long id) {
        Optional<Documentation> optDoc = documentationRepository.findById(id);
        if (optDoc.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Documentation doc = optDoc.get();
        doc.setActive(!doc.isActive());
        documentationRepository.save(doc);

        // ritorna l'oggetto aggiornato (o solo l'attributo active)
        return ResponseEntity.ok(Map.of("active", doc.isActive()));
    }

    // Endpoint per eliminare una documentazione tramite DELETE
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteDocumentation(@PathVariable Long id) {
        Optional<Documentation> docOpt = documentationRepository.findById(id);
        if (docOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Documentation not found");
        }
        documentationRepository.deleteById(id);
        return ResponseEntity.ok().body("Documentazione eliminata con successo.");
    }

}
