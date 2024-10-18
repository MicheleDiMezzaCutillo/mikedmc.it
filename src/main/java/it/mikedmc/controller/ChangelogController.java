package it.mikedmc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import it.mikedmc.model.Changelog;
import it.mikedmc.model.Log;
import it.mikedmc.model.User;
import it.mikedmc.service.ChangelogService;
import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/changelog")
public class ChangelogController {

    @Autowired
    private ChangelogService changelogService;

    @GetMapping
    public String listChangelogs(Model model, HttpServletRequest request) {
    	User loggedUser = (User) request.getSession().getAttribute("loggedUser");
        model.addAttribute("loggedUser", loggedUser);
        
        model.addAttribute("changelogs", changelogService.getAllChangelogs());
        return "changelog/changelog";
    }
    
    @GetMapping("/latest")
    public String showLatestChangelog(Model model, HttpServletRequest request) {
    	User loggedUser = (User) request.getSession().getAttribute("loggedUser");
        model.addAttribute("loggedUser", loggedUser);
        
        List<Changelog> list = new ArrayList<Changelog>();
        list.add(changelogService.getMostRecentChangelog());
        model.addAttribute("changelogs", list);
        return "changelog/changelog";
    }
    
    @GetMapping("/create")
    public String showCreateChangelogForm(Model model, HttpServletRequest request) {
    	User loggedUser = (User) request.getSession().getAttribute("loggedUser");
        model.addAttribute("loggedUser", loggedUser);
        
        Changelog cl = new Changelog();
        cl.setDate(LocalDate.now());
        model.addAttribute("changelog", cl);
        model.addAttribute("logs", List.of(new Log())); // Avvia con un log
        return "changelog/create-changelog";
    }

    @PostMapping("/create")
    public String createChangelog(
        @RequestParam("date") String dateString,
        @RequestParam("logMessages") List<String> logMessages
    ) {
        LocalDate date = LocalDate.parse(dateString);
        changelogService.createChangelog(date, logMessages);
        return "redirect:/changelog"; // Redirigi alla lista dei changelog
    }

    @GetMapping("/edit/{id}")
    public String showEditChangelogForm(@PathVariable Integer id, Model model, HttpServletRequest request) {
    	User loggedUser = (User) request.getSession().getAttribute("loggedUser");
        model.addAttribute("loggedUser", loggedUser);
        
        Changelog changelog = changelogService.getChangelogById(id);
        if (changelog != null) {
            model.addAttribute("changelog", changelog);
            model.addAttribute("logs", changelog.getLogs());
        }
        return "changelog/edit-changelog";
    }

    @PostMapping("/edit")
    public String editChangelog(
        @RequestParam("id") Integer id,
        @RequestParam("date") String dateString,
        @RequestParam("logMessages") List<String> logMessages
    ) {
        LocalDate date = LocalDate.parse(dateString);
        changelogService.updateChangelog(id, date, logMessages);
        return "redirect:/changelog"; // Redirigi alla lista dei changelog
    }

    @GetMapping("/delete/{id}")
    public String deleteChangelog(@PathVariable Integer id) {
        changelogService.deleteChangelog(id);
        return "redirect:/changelog"; // Redirigi alla lista dei changelog
    }
}