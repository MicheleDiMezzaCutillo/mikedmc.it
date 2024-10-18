package it.mikedmc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import it.mikedmc.enums.AofPlace;
import it.mikedmc.model.AofNpc;
import it.mikedmc.model.User;
import it.mikedmc.repository.AofNpcRepository;
import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/aofnpc")
public class AofNpcController {

    @Autowired
    private AofNpcRepository repository;

    // Lista di tutti gli NPC
    @GetMapping
    public String listAll(Model model, HttpServletRequest request) {
    	User loggedUser = (User) request.getSession().getAttribute("loggedUser");
        model.addAttribute("loggedUser", loggedUser);
    	
        model.addAttribute("npcs", repository.findAll());
        model.addAttribute("places", AofPlace.values());
        return "aof/npc/npc";
    }

    // Mostra la pagina per creare un nuovo NPC
    @GetMapping("/create")
    public String createNpcForm(Model model, HttpServletRequest request) {
    	User loggedUser = (User) request.getSession().getAttribute("loggedUser");
        model.addAttribute("loggedUser", loggedUser);
    	
        model.addAttribute("npc", new AofNpc());
        model.addAttribute("places", AofPlace.values());
        return "aof/npc/create-npc";
    }

    // Salva un nuovo NPC
    @PostMapping("/create")
    public String saveNpc(@ModelAttribute("npc") AofNpc npc) {
        repository.save(npc);
        return "redirect:/aofnpc";
    }

    // Mostra la pagina per modificare un NPC esistente
    @GetMapping("/edit/{id}")
    public String editNpcForm(@PathVariable Long id, Model model, HttpServletRequest request) {
    	User loggedUser = (User) request.getSession().getAttribute("loggedUser");
        model.addAttribute("loggedUser", loggedUser);
    	
        AofNpc npc = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid NPC ID:" + id));
        model.addAttribute("npc", npc);
        model.addAttribute("places", AofPlace.values());
        return "aof/npc/edit-npc";
    }

    // Salva le modifiche apportate a un NPC esistente
    @PostMapping("/edit/{id}")
    public String updateNpc(@PathVariable Long id, @ModelAttribute("npc") AofNpc npc) {
        npc.setId(id);
        repository.save(npc);
        return "redirect:/aofnpc";
    }
    
    @PostMapping("/delete/{id}")
    public String deleteNpc(@PathVariable Long id) {
        repository.deleteById(id);
        return "redirect:/aofnpc";
    }
}
