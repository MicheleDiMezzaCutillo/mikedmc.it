package it.mikedmc.controller;

import java.net.http.HttpRequest;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.mikedmc.model.Changelog;
import it.mikedmc.model.User;
import it.mikedmc.service.AdvertisingService;
import it.mikedmc.service.ChangelogService;
import it.mikedmc.service.UserService;
import it.mikedmc.lang.LangManager;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;

@Controller
public class HomeController {

	@Autowired
	private AdvertisingService advertisingService;
	
	@Autowired
	private ChangelogService changelogService;
	
    @GetMapping("/")
    public String home(Model model,HttpServletRequest request, @RequestParam(value = "lang", defaultValue = "it") String lang) {
    	User loggedUser = (User) request.getSession().getAttribute("loggedUser");
        model.addAttribute("loggedUser", loggedUser);
        model.addAttribute("advertisings",advertisingService.getAllAdvertising());
        
        Changelog changelog = changelogService.getLatestChangelog();
        if (changelog==null) {
            return LangManager.page("index",lang);
        }
        String formattedDate = changelog.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        changelog.setFormattedDate(formattedDate);

        model.addAttribute("changelog", changelog);
        if (!lang.equals("it")) {

        	switch(lang) {
        	case "en":
                if (loggedUser!=null) { // se l'utente è loggato:
                	model.addAttribute("title", "Warning");
                	model.addAttribute("message", "From now on the pages you see will not be translated, if necessary in the future it could be implemented.");
                	model.addAttribute("color", "yellow");
                }
        		break;
        	case "pl":
        		if (loggedUser != null) { // jeśli użytkownik jest zalogowany:
        	    model.addAttribute("title", "Ostrzeżenie");
        	    model.addAttribute("message", "Od teraz strony, które zobaczysz, nie będą tłumaczone. W razie potrzeby może to zostać wdrożone w przyszłości.");
        	    model.addAttribute("color", "yellow");
        	}

        		break;
        	case "ro":
        		if (loggedUser != null) { // dacă utilizatorul este autentificat:
        		    model.addAttribute("title", "Avertisment");
        		    model.addAttribute("message", "De acum înainte, paginile pe care le vezi nu vor fi traduse. Dacă va fi necesar, acest lucru ar putea fi implementat în viitor.");
        		    model.addAttribute("color", "yellow");
        		}
        		break;
        	}
        }
        return LangManager.page("index",lang);
    }
    
    @GetMapping("/termsAndConditions")
    public String termsAndConditions(HttpServletRequest request, Model model,@RequestParam(value = "lang", defaultValue = "it") String lang) {
    	
    	User loggedUser = (User) request.getSession().getAttribute("loggedUser");
        model.addAttribute("loggedUser", loggedUser);
    	return LangManager.page("legal/termsAndConditions",lang);
    }
    

    @GetMapping("/cookies")
    public String cookies(HttpServletRequest request, Model model,@RequestParam(value = "lang", defaultValue = "it") String lang) {
    	User loggedUser = (User) request.getSession().getAttribute("loggedUser");
        model.addAttribute("loggedUser", loggedUser);
        
		LangManager.getAttenzioneLinguaNonSupportata(lang,model);
    	return "legal/cookies";
    }
    
}
