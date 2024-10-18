package it.mikedmc.controller;

import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import it.mikedmc.model.Changelog;
import it.mikedmc.model.User;
import it.mikedmc.repository.ChangelogRepository;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class ChangelogsController {

	@Autowired
	ChangelogRepository changelogRepository;
	
	@GetMapping("/changelogs")
	public String changelogs (Model model, HttpServletRequest request) {
    	User loggedUser = (User) request.getSession().getAttribute("loggedUser");
        model.addAttribute("loggedUser", loggedUser);
        
		List<Changelog> changelogList = changelogRepository.findAllByOrderByDateDesc();
	    
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	    
	    changelogList.forEach(changelog -> {
	        changelog.setFormattedDate(changelog.getDate().format(formatter));
	    });
	    
	    model.addAttribute("changelogs",changelogList);
		return "changelogs";
	}
	
}
