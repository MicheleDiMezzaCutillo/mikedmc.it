package it.mikedmc.controller;

import it.mikedmc.enums.DocumentationContentType;
import it.mikedmc.model.Documentation;
import it.mikedmc.model.DocumentationContent;
import it.mikedmc.repository.DocumentationContentRepository;
import it.mikedmc.repository.DocumentationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/public/documentation")
public class DocumentationRestController {

    @Autowired
    private DocumentationRepository documentationRepository;

    @Autowired
    private DocumentationContentRepository documentationContentRepository;

    private final Path csvDirectory = Paths.get("website-data", "uploaded-csv");

    @GetMapping("/{code}/contents")
    public List<DocumentationContent> getContents(@PathVariable String code) {
        return documentationContentRepository.findByDocumentationCodeOrderByPositionAsc(code);
    }


    @GetMapping("/csv/{name}")
    public ResponseEntity<Resource> getCsv(@PathVariable String name) {
        try {
            // Evita accessi fuori directory con ../
            if (name.contains("..")) {
                return ResponseEntity.badRequest().build();
            }

            if (!name.endsWith(".csv")) {
                return ResponseEntity.badRequest().build();
            }

            Path file = csvDirectory.resolve(name);
            if (!Files.exists(file)) {
                return ResponseEntity.notFound().build();
            }

            Resource resource = new UrlResource(file.toUri());

            return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_PLAIN)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + name + "\"")
                    .body(resource);

        } catch (MalformedURLException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/test")
    public ResponseEntity<String> testting () {
        System.out.println("qualcosa");
        return ResponseEntity.ok("Funziona");
    }

}
