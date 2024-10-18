package it.mikedmc.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.mikedmc.model.Role;
import it.mikedmc.model.User;
import it.mikedmc.model.UserRole;
import it.mikedmc.repository.RoleRepository;
import it.mikedmc.repository.UserRepository;
import it.mikedmc.repository.UserRoleRepository;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private UserRoleRepository userRoleRepository;

    @GetMapping("/users")
    public String listUsers(Model model, HttpServletRequest request) {
    	User loggedUser = (User) request.getSession().getAttribute("loggedUser");
        model.addAttribute("loggedUser", loggedUser);
        
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "user/user";
    }

    @GetMapping("/users/active")
    public String listUsersActive(Model model, HttpServletRequest request) {
    	User loggedUser = (User) request.getSession().getAttribute("loggedUser");
        model.addAttribute("loggedUser", loggedUser);
        
    	LocalDateTime now = LocalDateTime.now().plusHours(2);
        LocalDateTime oneWeekAgo = now.minusWeeks(1);
        List<User> users = userRepository.findActiveUsers(oneWeekAgo, now);
        model.addAttribute("users", users);
        return "user/user";
    }

    @GetMapping("/users/new")
    public String listUsersNew(Model model, HttpServletRequest request) {
    	User loggedUser = (User) request.getSession().getAttribute("loggedUser");
        model.addAttribute("loggedUser", loggedUser);
        
    	LocalDateTime now = LocalDateTime.now().plusHours(2);
        LocalDateTime oneWeekAgo = now.minusWeeks(1);
        List<User> users = userRepository.findUsersCreatedBetween(oneWeekAgo, now);
        model.addAttribute("users", users);
        return "user/user";
    }

    @GetMapping("/users/confirmed")
    public String listUsersConfirmed(Model model, HttpServletRequest request) {
    	User loggedUser = (User) request.getSession().getAttribute("loggedUser");
        model.addAttribute("loggedUser", loggedUser);
        
        List<User> users = userRepository.findByConfirmedTrue();
        model.addAttribute("users", users);
        return "user/user";
    }

    @GetMapping("/users/notconfirmed")
    public String listUsersNotConfirmed(Model model, HttpServletRequest request) {
    	User loggedUser = (User) request.getSession().getAttribute("loggedUser");
        model.addAttribute("loggedUser", loggedUser);
        
        List<User> users = userRepository.findByConfirmedFalse();
        model.addAttribute("users", users);
        return "user/user";
    }
    
    @GetMapping("/users/edit/{id}")
    public String showEditUserForm(@PathVariable Long id, Model model, HttpServletRequest request) {
    	User loggedUser = (User) request.getSession().getAttribute("loggedUser");
        model.addAttribute("loggedUser", loggedUser);
        
        User user = userRepository.findById(id)
                                  .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        List<Role> allRoles = roleRepository.findAll();

        // Convert userRoles to a Set of Role IDs for easier lookup in the template
        Set<Long> userRoleIds = user.getUserRoles().stream()
                                    .map(ur -> ur.getRole().getId())
                                    .collect(Collectors.toSet());

        model.addAttribute("user", user);
        model.addAttribute("allRoles", allRoles);
        model.addAttribute("userRoleIds", userRoleIds);
        
        return "user/edit-user";
    }

    @PostMapping("/users/edit/{id}")
    public String updateUser(@PathVariable Long id,
                             @RequestParam("username") String username,
                             @RequestParam("email") String email,
                             @RequestParam(value="roles", defaultValue = "") List<Long> selectedRoleIds, // Ricevi la lista di ID dei ruoli selezionati
                             Model model) {

        // Recupera l'utente da aggiornare
        User user = userRepository.findById(id)
                          .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));

        // Aggiorna le proprietà dell'utente
        user.setUsername(username);
        user.setEmail(email);

        // creare una lista di UserRole con quelli che ha l'utente
        
        // Ottieni gli ID dei ruoli già associati all'utente
        Set<Long> userRoleIds = user.getUserRoles().stream()
                                          .map(ur -> ur.getRole().getId())
                                          .collect(Collectors.toSet());
        
        // Trova i ruoli da aggiungere
        Set<UserRole> rolesToAdd = selectedRoleIds.stream()    
        		.filter(roleId -> !userRoleIds.contains(roleId))
                .map(ur -> new Role(ur)) // ottengo i ruoli direttamente
                .map(role -> new UserRole(user,role))
                .collect(Collectors.toSet());
        
        Set<UserRole> rolesToRemove = user.getUserRoles().stream()
        		.filter(userRole -> !selectedRoleIds.contains(userRole.getRole().getId()))                
        		.collect(Collectors.toSet());
        
        if (rolesToAdd!=null) {
            Set<UserRole> tolto = user.getUserRoles();
            tolto.addAll(rolesToAdd);
            user.setUserRoles(tolto);
        }
        
        if (rolesToRemove!=null) {
            Set<UserRole> tolto = user.getUserRoles();
            rolesToRemove.stream()
            	.forEach(v -> {
            		tolto.remove(v);
            		userRoleRepository.deleteById(v.getId());
            	});
            user.setUserRoles(tolto);
        }
        
        userRepository.save(user);
        return "redirect:/users";
    }
    
    
    @PostMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id, Model model, HttpServletRequest request) {
    	// aggiungere controlli se l'utente ha un otp, o otpDiscord.
    	userRepository.deleteById(id);
        return "redirect:/users";
    }
}
