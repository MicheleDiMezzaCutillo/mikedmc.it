package it.mikedmc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import it.mikedmc.enums.AofPlace;
import it.mikedmc.enums.AofRarity;
import it.mikedmc.enums.AofRegion;
import it.mikedmc.model.AofDrop;
import it.mikedmc.model.AofMonster;
import it.mikedmc.model.AofNpc;
import it.mikedmc.model.User;
import it.mikedmc.repository.AofMonsterRepository;
import it.mikedmc.webhook.WebhookEmbedManager;
import it.mikedmc.webhook.WebhookLinkConfig;
import it.mikedmc.webhook.WebhookManager;
import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/aofmonster")
public class AofMonsterController {

	@Autowired
	private AofMonsterRepository repository;
	
	@GetMapping
    public String listAll(Model model, HttpServletRequest request) {
    	User loggedUser = (User) request.getSession().getAttribute("loggedUser");
        model.addAttribute("loggedUser", loggedUser);

        model.addAttribute("raritys", AofRarity.values());
        model.addAttribute("regions", AofRegion.values());
        model.addAttribute("monsters", repository.findAll());
        return "aof/loottable/aofmonster";
    }
	
	@GetMapping("/2")
    public String listAll2(Model model, HttpServletRequest request) {
    	User loggedUser = (User) request.getSession().getAttribute("loggedUser");
        model.addAttribute("loggedUser", loggedUser);

        model.addAttribute("raritys", AofRarity.values());
        model.addAttribute("regions", AofRegion.values());
        model.addAttribute("monsters", repository.findAll());
        return "aof/loottable/aofmonster2";
    }
	
	// Mostra la pagina per creare un nuovo NPC
    @GetMapping("/create")
    public String createAofMonsterForm(Model model, HttpServletRequest request) {
    	User loggedUser = (User) request.getSession().getAttribute("loggedUser");
        model.addAttribute("loggedUser", loggedUser);
    	
        model.addAttribute("monster", new AofMonster());
        model.addAttribute("regions", AofRegion.values());
        return "aof/loottable/create-aofmonster";
    }

    @PostMapping("/create")
    public String saveAofMonster(@ModelAttribute("monster") AofMonster monster, HttpServletRequest request) throws Exception {
    	User loggedUser = (User) request.getSession().getAttribute("loggedUser");
    	repository.save(monster);
    	
    	WebhookManager.sendWebhook(WebhookLinkConfig.mikedmcLoottable_modifiche, WebhookEmbedManager.creatoDemone(loggedUser, monster));
        return "redirect:/aofmonster";
    }
    
    @GetMapping("/edit/{id}")
    public String editAofMonsterForm(@PathVariable Long id, Model model, HttpServletRequest request) {
    	User loggedUser = (User) request.getSession().getAttribute("loggedUser");
        model.addAttribute("loggedUser", loggedUser);
    	
        AofMonster monster = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid NPC ID:" + id));
        model.addAttribute("monster", monster);
        model.addAttribute("drop", new AofDrop());
        model.addAttribute("regions", AofRegion.values());
        return "aof/loottable/edit-aofmonster";
    }

    // Salva le modifiche apportate a un NPC esistente
    @PostMapping("/edit/{id}")
    public String updateAofMonster(@PathVariable Long id, @ModelAttribute("monster") AofMonster monster, HttpServletRequest request) {
    	User loggedUser = (User) request.getSession().getAttribute("loggedUser");
        monster.setId(id);
        repository.save(monster);
        try {
			WebhookManager.sendWebhook(WebhookLinkConfig.mikedmcLoottable_modifiche, WebhookEmbedManager.modificatoDemone(loggedUser, monster));
		} catch (Exception e) {
			e.printStackTrace();
		}
        return "redirect:/aofmonster";
    }
    
    @GetMapping("/delete/{id}")
    public String deleteAofMonster(@PathVariable Long id, HttpServletRequest request) throws Exception {
    	User loggedUser = (User) request.getSession().getAttribute("loggedUser");
    	
    	WebhookManager.sendWebhook(WebhookLinkConfig.mikedmcLoottable_modifiche, WebhookEmbedManager.eliminatoDemone(loggedUser, id));
        repository.deleteById(id);
        return "redirect:/aofmonster";
    }
}
