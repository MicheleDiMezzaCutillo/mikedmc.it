package it.mikedmc.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.mikedmc.enums.AofRegion;
import it.mikedmc.model.AofMonster;
import it.mikedmc.model.Coupon;
import it.mikedmc.model.CouponRedeemed;
import it.mikedmc.model.CouponRole;
import it.mikedmc.model.Role;
import it.mikedmc.model.User;
import it.mikedmc.model.UserRole;
import it.mikedmc.repository.CouponRepository;
import it.mikedmc.repository.CouponRoleRepository;
import it.mikedmc.repository.RoleRepository;
import it.mikedmc.repository.UserRepository;
import it.mikedmc.repository.UserRoleRepository;
import it.mikedmc.service.CouponService;
import it.mikedmc.util.CodeGenerator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/coupon")
public class CouponController {
	
	@Autowired
	private CouponService couponService;
	
	@Autowired
	private UserRoleRepository userRoleRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private CouponRoleRepository couponRoleRepository;
	
	@GetMapping
    public String listAll(@RequestParam(defaultValue = "findAll") String filter, Model model, HttpServletRequest request) {
    	User loggedUser = (User) request.getSession().getAttribute("loggedUser");
        model.addAttribute("loggedUser", loggedUser);

        switch (filter) {
        case "findActive":
            model.addAttribute("coupons", couponService.findActiveCoupons());
            break;
        case "findInactive":
            model.addAttribute("coupons", couponService.findInactiveCoupons());
            break;
        default:
            model.addAttribute("coupons", couponService.findAll());
            break;
        }
        
		return "coupon/coupon";
	}
	
	@GetMapping("/find/{code}")
    public String find(@PathVariable("code") String code, Model model, HttpServletRequest request) {
    	User loggedUser = (User) request.getSession().getAttribute("loggedUser");
        model.addAttribute("loggedUser", loggedUser);
        Coupon coupon = couponService.findByCode(code);

        model.addAttribute("coupon",coupon);
		return "coupon/singleCoupon";
	}
	
	@PostMapping("/use")
    public String use(Model model, HttpServletRequest request, HttpServletResponse response, @RequestParam("code") String code) {
    	User loggedUser = (User) request.getSession().getAttribute("loggedUser");
        model.addAttribute("loggedUser", loggedUser);
        Coupon coupon = couponService.findByCode(code);
        
        if (coupon == null) {
        	model.addAttribute("loggedUser", loggedUser);
        	model.addAttribute("color","red");
        	model.addAttribute("title","Non esiste");
        	model.addAttribute("message","Il codice che hai fornito non è assocciato a nessun coupon.");
        	return "usersettings/settings";
        }
        
        LocalDateTime now = LocalDateTime.now().plusHours(2);
        
        if (coupon.isDisabled()) {
        	model.addAttribute("loggedUser", loggedUser);
        	model.addAttribute("color","red");
        	model.addAttribute("title","Disabilitato");
        	model.addAttribute("message","Il coupon che hai provato ad usare attualmente è disabilitato.");
        	return "usersettings/settings";
        }
        
        if (coupon.getCouponExpiration().isBefore(now)) {
        	model.addAttribute("loggedUser", loggedUser);
        	model.addAttribute("color","red");
        	model.addAttribute("title","Scaduto");
        	model.addAttribute("message","Il coupon che hai provato ad usare è scaduto.");
        	return "usersettings/settings";
        }
        
        if (coupon.getUses()==0) {
        	model.addAttribute("loggedUser", loggedUser);
        	model.addAttribute("color","red");
        	model.addAttribute("title","Terminato");
        	model.addAttribute("message","Il coupon che hai provato ad usare ha terminato gli utilizzi.");
        	return "usersettings/settings";
        }
        
        if (couponService.findByUserAndCoupon(loggedUser, coupon)) {
        	model.addAttribute("loggedUser", loggedUser);
        	model.addAttribute("color","red");
        	model.addAttribute("title","Già riscattato");
        	model.addAttribute("message","Il coupon che hai provato ad usare lo hai già riscattato.");
        	return "usersettings/settings";
        }
        
        if (coupon.isPermanent()) {
        	Set<Role> ur = loggedUser.getUserRoles().stream().map(urNow -> urNow.getRole()).collect(Collectors.toSet());
        	
        	List<Role> roleToAdd = new ArrayList<Role>();
        	
        	coupon.getCouponRoles().forEach(cr -> {
        		if (!ur.contains(cr.getRole())) 
        			roleToAdd.add(cr.getRole());
        	});
        	List<UserRole> ur2 = roleToAdd.stream().map(role -> new UserRole(loggedUser, role)).collect(Collectors.toList());
        	userRoleRepository.saveAll(ur2);
        	
            CouponRedeemed cr = new CouponRedeemed();
            cr.setCoupon(coupon);
            cr.setStartingDate(now);
            cr.setUser(loggedUser);
            cr.setFinishDate(now);
            couponService.saveNewCouponRedemption(cr);
            LogoutController.logoutForzato(request, response);
        	
            if (ur2.size()==0) {
            	request.getSession().setAttribute("message", "Hai riscattato il coupon, ma i ruoli che dovevo aggiungerti li hai già.");
            } else {
            	request.getSession().setAttribute("message", "Hai riscattato il coupon, aggiunti " + ur2.size() + " ruolo/i.");
            }
            
            request.getSession().setAttribute("title", "Successo");
            request.getSession().setAttribute("color","green");
            request.getSession().setAttribute("lang", "it");
            return "redirect:/login";
        }
        
        couponService.removeOneUse(coupon);
        
        CouponRedeemed cr = new CouponRedeemed();
        cr.setCoupon(coupon);
        cr.setStartingDate(now);
        cr.setUser(loggedUser);
        cr.setFinishDate(now.plusDays(coupon.getDurationDays()));
        couponService.saveNewCouponRedemption(cr);
        
        LogoutController.logoutForzato(request, response);
        request.getSession().setAttribute("title", "Successo!");
        request.getSession().setAttribute("message", "Hai riscattato il coupon ottenendo nei ruoli nuovi. Scadranno tra "+coupon.getDurationDays()+ " giorni. Adesso riesegui il login per usufruire dei nuovi ruoli.");
        request.getSession().setAttribute("color", "green");
        return "redirect:/login";
    }
	
    @GetMapping("/create")
    public String createForm(Model model, HttpServletRequest request) {
    	User loggedUser = (User) request.getSession().getAttribute("loggedUser");
        model.addAttribute("loggedUser", loggedUser);
    	
        model.addAttribute("roles",roleRepository.findAll());

        return "coupon/create-coupon";
    }
    
    @PostMapping("/create")
    public String create(Model model,
            @RequestParam("quantity") Integer quantity,
    		@RequestParam("code") String code,
            @RequestParam(value = "permanent", defaultValue = "false") Boolean permanent,
            @RequestParam("durationDays") Integer durationDays,
            @RequestParam("totalUses") Integer totalUses,
            @RequestParam("couponExpiration") String couponExpiration,
            @RequestParam(value = "roles", required = false) List<Long> roleIds,
            HttpServletRequest request) {
    	User loggedUser = (User) request.getSession().getAttribute("loggedUser");
        model.addAttribute("loggedUser", loggedUser);
    	
        for (int i = 0; i<quantity; i++) {
        	Coupon c = new Coupon();
        	c.setCode(code+"-"+CodeGenerator.generateCode(5));
        	c.setPermanent(permanent);
        	c.setDurationDays(durationDays);
        	c.setUses(totalUses);
        	c.setTotalUses(totalUses);
        	c.setCouponExpiration(LocalDateTime.parse(couponExpiration));
        	c.setCouponCreation(LocalDateTime.now().plusHours(2));
        	c = couponService.save(c);
        	Set<Role> roles = roleIds.stream().map(roleId -> roleRepository.findById(roleId).orElse(new Role())).collect(Collectors.toSet());
        	List<CouponRole> cr = new ArrayList<CouponRole>();
        	for (Role r : roles) {
        		cr.add(new CouponRole(c,r));
        	}
        	couponRoleRepository.saveAll(cr);
        }
        
        return "redirect:/coupon";
    }
    
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model, HttpServletRequest request) {
    	User loggedUser = (User) request.getSession().getAttribute("loggedUser");
        model.addAttribute("loggedUser", loggedUser);
        Coupon c = couponService.findById(id);
        model.addAttribute("coupon", c);
        model.addAttribute("roles",roleRepository.findAll());
        model.addAttribute("couponRoleIds",c.getCouponRoles().stream().map(cr -> cr.getRole().getId()).collect(Collectors.toList()));
        return "coupon/edit-coupon";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable Long id,
            @RequestParam("code") String code,
            @RequestParam(value = "permanent", defaultValue = "false") Boolean permanent,
            @RequestParam("durationDays") Integer durationDays,
            @RequestParam("uses") Integer uses,
            @RequestParam("totalUses") Integer totalUses,
            @RequestParam("couponExpiration") String couponExpiration,
            @RequestParam(value="roles", defaultValue = "") List<Long> roleIds,
    		HttpServletRequest request) {
    	User loggedUser = (User) request.getSession().getAttribute("loggedUser");
        
    	Coupon c = couponService.findById(id);
    	c.setCode(code);
    	c.setPermanent(permanent);
    	c.setDurationDays(durationDays);
    	c.setUses(uses);
    	c.setTotalUses(totalUses);
    	c.setCouponExpiration(LocalDateTime.parse(couponExpiration));
    	
    
		// Ottieni gli ID dei ruoli già associati all'utente
        Set<Long> userRoleIds = c.getCouponRoles().stream()
                                          .map(ur -> ur.getRole().getId())
                                          .collect(Collectors.toSet());
        
        // Trova i ruoli da aggiungere
        Set<CouponRole> rolesToAdd = roleIds.stream()    
        		.filter(roleId -> !userRoleIds.contains(roleId))
                .map(ur -> new Role(ur)) // ottengo i ruoli direttamente
                .map(role -> new CouponRole(c,role))
                .collect(Collectors.toSet());
        
        Set<CouponRole> rolesToRemove = c.getCouponRoles().stream()
        		.filter(couponRole -> !roleIds.contains(couponRole.getRole().getId()))                
        		.collect(Collectors.toSet());
		
        if (rolesToAdd!=null) {
        	Set<CouponRole> tolto = c.getCouponRoles();
            tolto.addAll(rolesToAdd);
            c.setCouponRoles(tolto);
        }
        
        if (rolesToRemove!=null) {
        	Set<CouponRole> tolto = c.getCouponRoles();
            rolesToRemove.stream()
            	.forEach(v -> {
            		tolto.remove(v);
            		couponRoleRepository.deleteById(v.getId());
            	});
            c.setCouponRoles(tolto);
        }
    	
    	couponService.save(c);
    	
        return "redirect:/coupon";
    }
	
    @GetMapping("/disable/{id}")
    public String disable(@PathVariable Long id, HttpServletRequest request) throws Exception {
    	User loggedUser = (User) request.getSession().getAttribute("loggedUser");
    	couponService.disable(id);
    	
        return "redirect:/coupon";
    }
    
    @GetMapping("/enable/{id}")
    public String enable(@PathVariable Long id, HttpServletRequest request) throws Exception {
    	User loggedUser = (User) request.getSession().getAttribute("loggedUser");
    	couponService.enable(id);
    	
        return "redirect:/coupon";
    }
    
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, HttpServletRequest request) throws Exception {
    	User loggedUser = (User) request.getSession().getAttribute("loggedUser");
    	couponService.deleteById(id);
        return "redirect:/coupon";
    }

}
