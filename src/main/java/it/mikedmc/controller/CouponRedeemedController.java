package it.mikedmc.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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

import it.mikedmc.dto.CouponRedeemedDto;
import it.mikedmc.enums.AofRegion;
import it.mikedmc.model.AofMonster;
import it.mikedmc.model.Coupon;
import it.mikedmc.model.CouponRedeemed;
import it.mikedmc.model.Role;
import it.mikedmc.model.User;
import it.mikedmc.model.UserRole;
import it.mikedmc.repository.UserRepository;
import it.mikedmc.repository.UserRoleRepository;
import it.mikedmc.service.CouponRedeemedService;
import it.mikedmc.service.CouponService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/couponRedeemed")
public class CouponRedeemedController {
	
	@Autowired
	private CouponRedeemedService couponRedeemedService;
	
	@Autowired
	private UserRoleRepository userRoleRepository;
	
	@GetMapping
    public String listAll(@RequestParam(defaultValue = "findAll") String filter, Model model, HttpServletRequest request) {
    	User loggedUser = (User) request.getSession().getAttribute("loggedUser");
        model.addAttribute("loggedUser", loggedUser);

        switch (filter) {
        case "findActive":
            model.addAttribute("couponRedeemeds", couponRedeemedService.findActiveCouponRedeemeds());
            break;
        case "findInactive":
            model.addAttribute("couponRedeemeds", couponRedeemedService.findInactiveCouponRedeemeds());
            break;
        default:
            model.addAttribute("couponRedeemeds", couponRedeemedService.findAll());
            break;
        }
        
		return "coupon/couponRedeemed";
	}
	
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model, HttpServletRequest request) {
    	User loggedUser = (User) request.getSession().getAttribute("loggedUser");
        model.addAttribute("loggedUser", loggedUser);
        CouponRedeemedDto crd = new CouponRedeemedDto();
        CouponRedeemed cr = couponRedeemedService.findById(id);
        crd.setId(id);

        crd.setActive(cr.isActive());
        crd.setFinishDate(cr.getFinishDate());
        crd.setStartingDate(cr.getStartingDate());
        model.addAttribute("couponRedeemed",crd);
        return "coupon/edit-couponRedeemed";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable Long id,
            @RequestParam("startingDate") String startingDate,
            @RequestParam("finishDate") String finishDate,
            HttpServletRequest request) {
    	User loggedUser = (User) request.getSession().getAttribute("loggedUser");
        CouponRedeemed cr = couponRedeemedService.findById(id);
        cr.setFinishDate(LocalDateTime.parse(finishDate));
        cr.setStartingDate(LocalDateTime.parse(startingDate));
    	couponRedeemedService.save(cr);
        return "redirect:/couponRedeemed";
    }
    
    @GetMapping("/disable/{id}")
    public String disable(@PathVariable Long id, HttpServletRequest request) throws Exception {
    	User loggedUser = (User) request.getSession().getAttribute("loggedUser");
    	couponRedeemedService.disable(id);
    	
        return "redirect:/couponRedeemed";
    }
    
    @GetMapping("/enable/{id}")
    public String enable(@PathVariable Long id, HttpServletRequest request) throws Exception {
    	User loggedUser = (User) request.getSession().getAttribute("loggedUser");
    	couponRedeemedService.enable(id);
    	
        return "redirect:/couponRedeemed";
    }
    
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, HttpServletRequest request) throws Exception {
    	User loggedUser = (User) request.getSession().getAttribute("loggedUser");
    	couponRedeemedService.deleteById(id);
    	
        return "redirect:/couponRedeemed";
    }

}
