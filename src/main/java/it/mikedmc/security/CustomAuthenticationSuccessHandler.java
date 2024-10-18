package it.mikedmc.security;


import it.mikedmc.lang.LangManager;
import it.mikedmc.model.CouponRedeemed;
import it.mikedmc.model.CouponRole;
import it.mikedmc.model.Role;
import it.mikedmc.model.User;
import it.mikedmc.model.UserRole;
import it.mikedmc.repository.UserRepository;
import it.mikedmc.service.CouponRedeemedService;
import it.mikedmc.webhook.WebhookEmbedManager;
import it.mikedmc.webhook.WebhookLinkConfig;
import it.mikedmc.webhook.WebhookManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import java.awt.image.renderable.RenderableImage;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	@Autowired
	private CouponRedeemedService couponRedeemedService;
	
	@Autowired
    private final UserRepository userRepository;

    public CustomAuthenticationSuccessHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
    		org.springframework.security.core.Authentication authentication) throws IOException, ServletException {
    	
    	String lang = (String) request.getSession().getAttribute("lang");
    	Object principal = authentication.getPrincipal();
    	String username;

    	if (principal instanceof UserDetails) {
    	    username = ((UserDetails) principal).getUsername(); // Ottieni il nome utente
    	} else {
    	    username = principal.toString(); // Gestisci il caso in cui non sia UserDetails
    	}
        User user = userRepository.findByUsernameOrEmailAndConfirmed(username);
        user.setLastLoginDate(LocalDateTime.now().plusHours(2));
        userRepository.save(user);
        
		try {
			WebhookManager.sendWebhook(WebhookLinkConfig.mikedmcLogin_utenti, WebhookEmbedManager.embedLogin(
					user.getUsername(),
					user.getEmail(),
					user.getEmail(),
					user.getIdDiscord(),
					user.getLinkDiscordProfile()));
		} catch (Exception e) {
			System.out.println("Log del login fallito.");
			e.printStackTrace();
		}
        
		
		 Set<UserRole> userRuoli = user.getUserRoles();
		 Set<Long> ruoli = user.getUserRoles().stream().map(ur -> ur.getRole().getId()).collect(Collectors.toSet());
         
         List<CouponRedeemed> ruoliExtra = couponRedeemedService.findCouponRoles(user);
         if (ruoliExtra != null) {
         	for (CouponRedeemed c : ruoliExtra) {
         		for (CouponRole cr : c.getCoupon().getCouponRoles()) {
         			if (!ruoli.contains(cr.getRole().getId())) {
         				userRuoli.add(new UserRole(new User(),cr.getRole()));         				
         			}
         		}
         	}
         	
         }
         
         user.setUserRoles(userRuoli);
		
        request.getSession().setAttribute("loggedUser", user);
        
        String redirectUrl = (String) request.getSession().getAttribute("loginAfterUrl");
        if (redirectUrl!=null) {
        	response.sendRedirect(LangManager.page(request.getContextPath()+redirectUrl,lang));
        	request.getSession().removeAttribute("loginAfterUrl");
        	return;
        }
        
    	switch(lang) {
    	case "en":
            response.sendRedirect(request.getContextPath() + "/?lang=en");

    		break;
    	case "pl":
            response.sendRedirect(request.getContextPath() + "/?lang=pl");

    		break;
    	case "ro":
            response.sendRedirect(request.getContextPath() + "/?lang=ro");

    		break;
		default:
	        response.sendRedirect(request.getContextPath() + "/");
			break;
    	}
    }
}
