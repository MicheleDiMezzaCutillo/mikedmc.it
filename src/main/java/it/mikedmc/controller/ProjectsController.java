package it.mikedmc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.mikedmc.model.User;
import it.mikedmc.lang.LangManager;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class ProjectsController {
	@GetMapping("/progetti")
	public String progetti( Model model, HttpServletRequest request, @RequestParam(value = "lang", defaultValue = "it") String lang) {
    	User loggedUser = (User) request.getSession().getAttribute("loggedUser");
        model.addAttribute("loggedUser", loggedUser);
        
        switch(lang) {
        case "en":
            model.addAttribute("title", "Info!");
            model.addAttribute("message", "Click on the images to learn more, click again to close them.");
            model.addAttribute("color", "blue");
            break;
        case "pl":
            model.addAttribute("title", "Informacja!");
            model.addAttribute("message", "Kliknij na obrazki, aby dowiedzieć się więcej, kliknij ponownie, aby je zamknąć.");
            model.addAttribute("color", "blue");
            break;
        case "ro":
            model.addAttribute("title", "Informații!");
            model.addAttribute("message", "Apasă pe imagini pentru a afla mai multe, apasă din nou pentru a le închide.");
            model.addAttribute("color", "blue");
            break;
    	default:
    		model.addAttribute("title", "Info!");
            model.addAttribute("message", "Premi le immagini per saperne di più, ripremi per chiuderle.");
            model.addAttribute("color", "blue");
    	}
	    return LangManager.page("projects/progetti",lang);
	}
}