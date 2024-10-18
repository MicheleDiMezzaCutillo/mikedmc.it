package it.mikedmc.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.mikedmc.model.MusicanzaMenu;
import it.mikedmc.model.User;
import it.mikedmc.repository.MusicanzaRepository;
import it.mikedmc.security.OTPCodeGenerator;
import it.mikedmc.service.OtpDiscordService;
import it.mikedmc.util.EmailManager;
import it.mikedmc.webhook.WebhookEmbedManager;
import it.mikedmc.webhook.WebhookLinkConfig;
import it.mikedmc.webhook.WebhookManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@Controller
@RequestMapping("/settings")
public class UserSettingsController {
	
    @GetMapping
    public String userSettings (
    		HttpServletRequest request,
    		@RequestParam(name = "id", defaultValue = "1") String id, 
    		Model model) {
    	User loggedUser = (User) request.getSession().getAttribute("loggedUser");
    	model.addAttribute("loggedUser", loggedUser);

        switch (id) {
        case "2":
        	String discordId2 = loggedUser.getIdDiscord();
        	if (discordId2==null || discordId2.isBlank() || discordId2.isEmpty()) {
        		model.addAttribute("title", "Attenzione");
    	        model.addAttribute("message", "Per accedere a questa pagina, devi aver prima collegato il tuo account Discord!");
    	        model.addAttribute("color", "red");
    		
            	return "usersettings/settings";	
        	}
        	
        	MusicanzaRepository musicanzaRepository2 = MusicanzaRepository.getIstance();
        	
        	List<MusicanzaMenu> mr = musicanzaRepository2.getMenuRowsByUser(discordId2);
        	if (mr == null) {
        		model.addAttribute("title", "Attenzione");
        		model.addAttribute("message", "Purtroppo non hai mai salvato nessuna canzone con Musicanza, torna quando lo avrai fatto.");
        		model.addAttribute("color", "yellow");
        		
        		return "usersettings/settings";	
        	}

        	model.addAttribute("musicanzaMenus",mr);
        	
        	String server = musicanzaRepository2.getNomeServerByUserIdWithMaxContatore(discordId2);
        	
        	model.addAttribute("countComandi",musicanzaRepository2.getTotalContatoreByUserId(discordId2));
        	model.addAttribute("server",server);
        	model.addAttribute("countComandiServer",musicanzaRepository2.getTotaleContatoreByUserIdAndNomeServer(discordId2, server));
        	
        	return "usersettings/settings2";
        case "3":
        	String discordId3 = loggedUser.getIdDiscord();
        	if (discordId3==null || discordId3.isBlank() || discordId3.isEmpty()) {
        		model.addAttribute("title", "Attenzione");
    	        model.addAttribute("message", "Per accedere a questa pagina, devi aver prima collegato il tuo account Discord!");
    	        model.addAttribute("color", "red");
    	        
            	return "usersettings/settings";	
        	}
        	
        	if (3>2) {
        		model.addAttribute("title", "Attenzione");
    	        model.addAttribute("message", "Questa pagina ancora non è attiva.");
    	        model.addAttribute("color", "yellow");
            	return "usersettings/settings";	
        	}
        	
        	MusicanzaRepository musicanzaRepository3 = MusicanzaRepository.getIstance();
        	return "usersettings/settings3";
        default:
        	return "usersettings/settings";	
	    }
    }
    
    @GetMapping("/musicanza/delete/{id}")
    public String musicanzaDelete (@PathVariable Integer id, HttpServletRequest request, HttpServletResponse response,Model model ) throws Exception {
    	User loggedUser = (User) request.getSession().getAttribute("loggedUser");
    	model.addAttribute("loggedUser", loggedUser);
    	MusicanzaRepository musicanzaRepository = MusicanzaRepository.getIstance();
    	
    	if(!musicanzaRepository.deleteById(id, loggedUser.getIdDiscord())) {
    		model.addAttribute("title", "Errore");
	        model.addAttribute("message", "Per un errore non è stato possibile eliminare questa canzone dal tuo menù.");
	        model.addAttribute("color", "red");
    	}
    	
    	return "redirect:/settings?id=2";	
    }
    
    @Autowired
    private OtpDiscordService otpDiscordService;
	
    @PostMapping("/linkDiscord")
    public String userSettingsLinkDiscord (HttpServletRequest request, HttpServletResponse response,Model model ) throws Exception {
    	
    	User loggedUser = (User) request.getSession().getAttribute("loggedUser");
    	
		// manda email con codice otp, aggiungi codice otp al database.
    	String otpCode = OTPCodeGenerator.generateOTPCode();
        
    	otpDiscordService.upsert(loggedUser, otpCode);
        
        EmailManager.sendMailDiscordLink(loggedUser.getEmail(), loggedUser.getUsername(), otpCode, "it");
        
        LogoutController.logoutForzato(request,response);
        model.addAttribute("title", "Info");
        model.addAttribute("message", "Ti ho mandato le istruzioni all'email, seguile e poi riesegui il login.");
        model.addAttribute("color", "blue");
        
		model.addAttribute("loggedUser", loggedUser);
		return "index";
    }
    
    @PostMapping("/sendMessage")
    public String userSettingsMessage (HttpServletRequest request,
    		HttpServletResponse response,
    		Model model, 
            @RequestParam(value = "operazione", required = true) String operazione,
            @RequestParam(value = "messaggio", required = false) String messaggio,
            @RequestParam(value = "email", required = false) String email
    		) throws Exception {
    	
    	User loggedUser = (User) request.getSession().getAttribute("loggedUser");
    	
    	
    		WebhookManager.sendWebhook(WebhookLinkConfig.mikedmcMessaggi_sito, WebhookEmbedManager.embedContattami(
    				loggedUser.getUsername(),
					loggedUser.getEmail(),
					email,loggedUser.getIdDiscord(),
					loggedUser.getLinkDiscordProfile(),
					messaggio));
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // Modifica il formato come desiderato
        String formattedDate = loggedUser.getCreatedDate().format(formatter);
    	
        model.addAttribute("title", "Successo");
		model.addAttribute("message", "Il messaggio è stato inviato.");
		model.addAttribute("color", "green");
        
    	model.addAttribute("loggedUser", loggedUser);
    	model.addAttribute("formattedDate", formattedDate);
    	return "usersettings/settings";	
    }
    
}
