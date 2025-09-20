package it.mikedmc.controller;

import it.mikedmc.model.Documentation;
import it.mikedmc.model.DocumentationContent;
import it.mikedmc.model.User;
import it.mikedmc.repository.DocumentationContentRepository;
import it.mikedmc.repository.DocumentationRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class DocumentationController {

    @Autowired
    private DocumentationRepository documentationRepository;

    @Autowired
    private DocumentationContentRepository documentationContentRepository;

    @GetMapping("/docs")
    public String publicDocs (Model model) {
        model.addAttribute("projects", documentationRepository.findByIsPublicTrueAndIsActiveTrue());

        return "documentation/docs";
    }

    @GetMapping("/docs/{projectCode}")
    public String documentationInfo(
            @PathVariable("projectCode") String code,
            @RequestParam(name = "password", required = false) String password,
            Model model,
            RedirectAttributes redirectAttributes) {

        Documentation documentation = documentationRepository.findByCode(code);

        if (documentation == null) {
            // Se non trovi la documentazione, reindirizza a /docs (lista)

            model.addAttribute("color", "red");
            model.addAttribute("title", "Errore!");
            model.addAttribute("message", "La documentazione non è stata trovata.");

            model.addAttribute("projects", documentationRepository.findByIsPublicTrueAndIsActiveTrue());

            return "documentation/docs";
        }

        if (documentation.isPublic()) {
            // Se è pubblica, mostra la pagina
            model.addAttribute("project", documentation);
            return "documentation/docs-page";
        } else {
            // Non è pubblica: verifica la password
            if (password != null && password.equals(documentation.getPassword())) {
                model.addAttribute("project", documentation);
                return "documentation/docs-page";
            } else {
                // Password mancante o errata, reindirizza a /docs
                model.addAttribute("color", "red");
                model.addAttribute("title", "Errore!");
                model.addAttribute("message", "Password mancante o errata.");

                model.addAttribute("projects", documentationRepository.findByIsPublicTrueAndIsActiveTrue());

                return "documentation/docs";
            }
        }
    }


    @GetMapping("/documentation/list")
    public String listDocumentations(Model model, HttpServletRequest request) {
        User loggedUser = (User) request.getSession().getAttribute("loggedUser");
        model.addAttribute("loggedUser", loggedUser);

        List<Documentation> docs = documentationRepository.findAll(); // o repository.findAll()
        model.addAttribute("documentations", docs);
        return "documentation/list";
    }

    @GetMapping("/documentation/create")
    public String createDocumentation(Model model, HttpServletRequest request) {
        User loggedUser = (User) request.getSession().getAttribute("loggedUser");
        model.addAttribute("loggedUser", loggedUser);

        model.addAttribute("documentation", new Documentation());
        return "documentation/create";
    }


    @GetMapping("/documentation/edit/{id}")
    public String editDocumentation(@PathVariable Long id, HttpServletRequest request, Model model) {
        User loggedUser = (User) request.getSession().getAttribute("loggedUser");
        model.addAttribute("loggedUser", loggedUser);

        Documentation doc = documentationRepository.findById(id).orElse(new Documentation());
        model.addAttribute("documentation", doc);

        // Carica contenuti ordinati direttamente dal DB
        List<DocumentationContent> orderedContents = documentationContentRepository.findByDocumentationCodeOrderByPositionAsc(doc.getCode());
        model.addAttribute("contents", orderedContents);

        return "documentation/edit";
    }

}
