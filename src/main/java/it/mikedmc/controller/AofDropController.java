package it.mikedmc.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.mikedmc.enums.AofRarity;
import it.mikedmc.model.AofDrop;
import it.mikedmc.model.AofMonster;
import it.mikedmc.model.User;
import it.mikedmc.repository.AofDropRepository;
import it.mikedmc.repository.AofMonsterRepository;
import it.mikedmc.webhook.WebhookEmbedManager;
import it.mikedmc.webhook.WebhookLinkConfig;
import it.mikedmc.webhook.WebhookManager;
import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/aofdrop")
public class AofDropController {
	
	@Autowired
	private AofMonsterRepository aofMonsterRepository;
	
	@Autowired
	private AofDropRepository aofDropRepository;
	
	// Mostra il form per creare un nuovo drop associato a un mostro
    @GetMapping("/create/{id}")
    public String showCreateForm(@PathVariable("id") Long id, Model model, HttpServletRequest request) {
    	User loggedUser = (User) request.getSession().getAttribute("loggedUser");
        model.addAttribute("loggedUser", loggedUser);
    	
        AofDrop drop = new AofDrop();
        AofMonster am = aofMonsterRepository.findById(id).orElse(new AofMonster());
        drop.setAofMonster(am);
        model.addAttribute("raritys", AofRarity.values());
        model.addAttribute("drop", drop);

        return "aof/loottable/create-aofdrop";  // Nome del template Thymeleaf per la creazione
    }

    // Gestisce il submit del form di creazione
    @PostMapping("/create")
    public String createDrop(@ModelAttribute("drop") AofDrop drop, HttpServletRequest request) {
    	User loggedUser = (User) request.getSession().getAttribute("loggedUser"); 	
    	drop = aofDropRepository.save(drop);
    	try {
			WebhookManager.sendWebhook(WebhookLinkConfig.mikedmcLoottable_modifiche, WebhookEmbedManager.aggiuntoDropDemone(loggedUser, drop));
		} catch (Exception e) {
			e.printStackTrace();
		}
        return "redirect:/aofmonster/edit/"+drop.getAofMonster().getId();  // Reindirizza alla lista dei mostri dopo la creazione
    }

    // Mostra il form per modificare un drop esistente
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model, HttpServletRequest request) {
    	User loggedUser = (User) request.getSession().getAttribute("loggedUser");
        model.addAttribute("loggedUser", loggedUser);
    	
        AofDrop drop = aofDropRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid drop ID: " + id));
        model.addAttribute("raritys", AofRarity.values());
        model.addAttribute("drop", drop);
        return "aof/loottable/edit-aofdrop";  // Nome del template Thymeleaf per la modifica
    }

    // Gestisce il submit del form di modifica
    @PostMapping("/edit/{id}")
    public String updateDrop(@PathVariable("id") Long id, @ModelAttribute("drop") AofDrop drop, HttpServletRequest request) {
    	User loggedUser = (User) request.getSession().getAttribute("loggedUser");
    	aofDropRepository.save(drop);
    	
    	try {
			WebhookManager.sendWebhook(WebhookLinkConfig.mikedmcLoottable_modifiche, WebhookEmbedManager.modificatoDropDemone(loggedUser, drop));
		} catch (Exception e) {
			e.printStackTrace();
		}
        return "redirect:/aofmonster/edit/"+drop.getAofMonster().getId();  // Reindirizza alla lista dei mostri dopo l'aggiornamento
    }
    
    @GetMapping("/delete/{id}")
    public String deleteDrop(@PathVariable Long id, HttpServletRequest request) {
    	User loggedUser = (User) request.getSession().getAttribute("loggedUser");
        AofDrop drop = aofDropRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid drop ID: " + id));

        Long monsterId = drop.getAofMonster().getId();  // Recupera l'ID del mostro associato

        try {
			WebhookManager.sendWebhook(WebhookLinkConfig.mikedmcLoottable_modifiche, WebhookEmbedManager.eliminatoDropDemone(loggedUser, drop));
		} catch (Exception e) {
			e.printStackTrace();
		}
        aofDropRepository.delete(drop);  // Elimina il drop
        return "redirect:/aofmonster/edit/" + monsterId;  // Reindirizza alla pagina di modifica del mostro
    }
}
