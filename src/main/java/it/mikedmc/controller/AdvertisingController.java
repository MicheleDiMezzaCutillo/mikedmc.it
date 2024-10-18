package it.mikedmc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import it.mikedmc.model.Advertising;
import it.mikedmc.model.User;
import it.mikedmc.service.AdvertisingService;
import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/advertising")
public class AdvertisingController {
	
	@Autowired
	private AdvertisingService advertisingService;

    @GetMapping
    public String listAdvertisings(Model model, HttpServletRequest request) {
    	User loggedUser = (User) request.getSession().getAttribute("loggedUser");
        model.addAttribute("loggedUser", loggedUser);
        
        model.addAttribute("advertisings", advertisingService.getAllAdvertising());
        return "advertising/advertising";
    }
    
    @GetMapping("/create")
    public String showCreateAdvertisingForm(Model model, HttpServletRequest request) {
    	User loggedUser = (User) request.getSession().getAttribute("loggedUser");
        model.addAttribute("loggedUser", loggedUser);
        
        model.addAttribute("advertising", new Advertising());
        return "advertising/create-advertising";
    }

    @PostMapping("/create")
    public String createAdvertising(@ModelAttribute Advertising advertising) {
        advertisingService.createAdvertising(advertising);  // Salva direttamente l'oggetto Advertising
        return "redirect:/advertising"; // Redirige alla lista degli advertising
    }

    @GetMapping("/edit/{id}")
    public String showEditAdvertisingForm(@PathVariable Integer id, Model model, HttpServletRequest request) {
    	User loggedUser = (User) request.getSession().getAttribute("loggedUser");
        model.addAttribute("loggedUser", loggedUser);
    	
    	Advertising advertising = advertisingService.getAdvertisingById(id);
        if (advertising != null) {
            model.addAttribute("advertising", advertising);
            return "advertising/edit-advertising";
        } else {
            return "redirect:/advertising";
        }
    }

    @PostMapping("/edit")
    public String editAdvertising(@ModelAttribute Advertising advertising) {
        advertisingService.updateAdvertising(advertising);
        return "redirect:/advertising";
    }

    @GetMapping("/delete/{id}")
    public String deleteAdvertising(@PathVariable Integer id) {
    	advertisingService.deleteAdvertising(id);
        return "redirect:/advertising";
    }
}
