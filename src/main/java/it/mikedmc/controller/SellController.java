package it.mikedmc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.mikedmc.model.User;
import it.mikedmc.lang.LangManager;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class SellController {
	
	@GetMapping("/discordbot")
	public String botDiscord(Model model,HttpServletRequest request, @RequestParam(value = "lang", defaultValue = "it") String lang) {
    	User loggedUser = (User) request.getSession().getAttribute("loggedUser");
        model.addAttribute("loggedUser", loggedUser);
        return LangManager.page("sell/discordbot",lang);
	}

	@GetMapping("/discordserver")
		public String serverDiscord(Model model,HttpServletRequest request, @RequestParam(value = "lang", defaultValue = "it") String lang) {
	    	User loggedUser = (User) request.getSession().getAttribute("loggedUser");
	        model.addAttribute("loggedUser", loggedUser);
	    
	        
	    return LangManager.page("sell/discordserver",lang);
	}

	@GetMapping("/telegrambot")
	public String botTelegram(Model model,HttpServletRequest request, @RequestParam(value = "lang", defaultValue = "it") String lang) {
    	User loggedUser = (User) request.getSession().getAttribute("loggedUser");
        model.addAttribute("loggedUser", loggedUser);
	    return LangManager.page("sell/telegrambot",lang);
	}

	@GetMapping("/niente")
	public String niente(Model model,HttpServletRequest request, @RequestParam(value = "lang", defaultValue = "it") String lang) {
    	User loggedUser = (User) request.getSession().getAttribute("loggedUser");
        model.addAttribute("loggedUser", loggedUser);
	    return LangManager.page("sell/niente",lang);
	}
	
	
}
