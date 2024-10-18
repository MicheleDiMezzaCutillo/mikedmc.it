package it.mikedmc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import it.mikedmc.model.User;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class ErrorController {

    @RequestMapping("/403")
    public String error403(Model model,HttpServletRequest request) {
    	User loggedUser = (User) request.getSession().getAttribute("loggedUser");
        model.addAttribute("loggedUser", loggedUser);
        
        return "error/403";
    }
    
    @RequestMapping("/404")
    public String error404(Model model, HttpServletRequest request) {
        User loggedUser = (User) request.getSession().getAttribute("loggedUser");
        model.addAttribute("loggedUser", loggedUser);
        return "error/404";
    }
    
    @GetMapping("/login-required")
    public String loginRequired(Model model, HttpServletRequest request) {
    	User loggedUser = (User) request.getSession().getAttribute("loggedUser");
        model.addAttribute("loggedUser", loggedUser);
        
        return "error/login-required"; // Nome del template Thymeleaf per la pagina di errore "Login Richiesto"
    }
}
