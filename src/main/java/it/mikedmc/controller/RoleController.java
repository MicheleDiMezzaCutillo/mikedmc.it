package it.mikedmc.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import it.mikedmc.model.Role;
import it.mikedmc.model.User;
import it.mikedmc.repository.RoleRepository;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class RoleController {

    @Autowired
    private RoleRepository roleRepository;

    @GetMapping("/roles")
    public String listRoles(Model model, HttpServletRequest request) {
    	User loggedUser = (User) request.getSession().getAttribute("loggedUser");
        model.addAttribute("loggedUser", loggedUser);
        
    	
    	
        List<Role> roles = roleRepository.findAll();
        model.addAttribute("roles", roles);
        return "roles/roles";
    }

    @GetMapping("/roles/create")
    public String showCreateRoleForm(Model model , HttpServletRequest request) {
    	User loggedUser = (User) request.getSession().getAttribute("loggedUser");
        model.addAttribute("loggedUser", loggedUser);
        
        model.addAttribute("role", new Role());
        return "roles/create-role";
    }

    @PostMapping("/roles/create")
    public String createRole(@ModelAttribute Role role, BindingResult result, Model model, HttpServletRequest request) {
    	User loggedUser = (User) request.getSession().getAttribute("loggedUser");
        model.addAttribute("loggedUser", loggedUser);
        
        if (result.hasErrors()) {
            model.addAttribute("error", "Error in form submission");
            return "roles/create-role";
        }
        try {
            roleRepository.save(role);
            return "redirect:/roles";
        } catch (Exception e) {
            model.addAttribute("error", "An error occurred while creating the role: " + e.getMessage());
            return "roles/create-role";
        }
    }

    @GetMapping("/roles/edit/{id}")
    public String showEditRoleForm(@PathVariable Long id, Model model, HttpServletRequest request) {
    	User loggedUser = (User) request.getSession().getAttribute("loggedUser");
        model.addAttribute("loggedUser", loggedUser);
        
        Role role = roleRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid role Id:" + id));
        model.addAttribute("role", role);
        model.addAttribute("error", null); // Initialize error attribute
        return "roles/edit-role";
    }

    @PostMapping("/roles/edit/{id}")
    public String updateRole(@PathVariable Long id, @ModelAttribute Role role, BindingResult result, Model model, HttpServletRequest request) {
    	User loggedUser = (User) request.getSession().getAttribute("loggedUser");
        model.addAttribute("loggedUser", loggedUser);
        
        if (result.hasErrors()) {
            model.addAttribute("error", "Error in form submission");
            return "roles/edit-role";
        }
        try {
            role.setId(id);
            roleRepository.save(role);
            return "redirect:/roles";
        } catch (Exception e) {
            model.addAttribute("error", "An error occurred while updating the role: " + e.getMessage());
            return "roles/edit-role";
        }
    }

    @PostMapping("/roles/delete/{id}")
    public String deleteRole(@PathVariable Long id, Model model) {
    	
        try {
            roleRepository.deleteById(id);
        } catch (Exception e) {
            model.addAttribute("error", "An error occurred while deleting the role: " + e.getMessage());
        }
        return "redirect:/roles";
    }
}
