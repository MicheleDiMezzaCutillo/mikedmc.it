package it.mikedmc.controller;

import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.mikedmc.lang.LangManager;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class LogoutController {

    @GetMapping("/logout") // logout custom
    public String customLogout(HttpServletRequest request, HttpServletResponse response, Model model, @RequestParam(value = "lang", defaultValue = "it") String lang) throws IOException {
    	
        // Invalidare la sessione
        request.getSession().invalidate();
        
        // Eliminare i cookie di sessione
        Cookie cookie = new Cookie("JSESSIONID", null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        
    	switch(lang) { // langSwitch
			case "en":
		    	model.addAttribute("title", "Logout");
		    	model.addAttribute("message", "You've been logged out, come back soon.");
		    	model.addAttribute("color", "yellow");
				break;
			case "pl":
				model.addAttribute("title", "Wyloguj");
		    	model.addAttribute("message", "Zostałeś wylogowany. Wróć wkrótce.");
		    	model.addAttribute("color", "yellow");
				break;
			case "ro":
				model.addAttribute("title", "Deconectare");
		    	model.addAttribute("message", "Ați fost deconectat, reveniți curând.");
		    	model.addAttribute("color", "yellow");
				break;
			default:
				model.addAttribute("title", "Logout");
		        model.addAttribute("message", "Hai effettuato il logout, torna presto.");
		        model.addAttribute("color", "yellow");
		        break;
    	}
        
        
        request.getSession().setAttribute("lang", lang);
        // Reindirizzare alla pagina di login
        return LangManager.page("login/login",lang);
    }
    
    public static void logoutForzato(HttpServletRequest request, HttpServletResponse response) {
    	// Invalidare la sessione
        request.getSession().invalidate();
        
        // Eliminare i cookie di sessione
        Cookie cookie = new Cookie("JSESSIONID", null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}