package it.mikedmc.controller;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.mikedmc.model.Otp;
import it.mikedmc.model.Role;
import it.mikedmc.model.User;
import it.mikedmc.security.OTPCodeGenerator;
import it.mikedmc.service.OtpService;
import it.mikedmc.service.UserService;
import it.mikedmc.util.EmailManager;
import it.mikedmc.webhook.WebhookEmbedManager;
import it.mikedmc.webhook.WebhookLinkConfig;
import it.mikedmc.webhook.WebhookManager;
import it.mikedmc.dto.UserDto;
import it.mikedmc.lang.LangManager;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private OtpService otpService;

    /*
    @GetMapping("/test")
    public String test(Model model, HttpServletRequest request, @RequestParam(value = "lang", defaultValue = "it") String lang) {
    	return "rendirect:/";
    }
    */
    
    @GetMapping("/login")
    public String login(@RequestParam(required = false) String title,
                        @RequestParam(required = false) String message,
                        @RequestParam(required = false) String color,
                        Model model, HttpServletRequest request, @RequestParam(value = "lang", defaultValue = "it") String lang) {
    	User loggedUser = (User) request.getSession().getAttribute("loggedUser");
        model.addAttribute("loggedUser", loggedUser);
        request.getSession().setAttribute("lang", lang);
        
        String titolo = (String) request.getSession().getAttribute("title");
        if (titolo!=null) {
        	model.addAttribute("title", titolo);
            model.addAttribute("message", (String) request.getSession().getAttribute("message"));
            model.addAttribute("color", (String) request.getSession().getAttribute("color"));
            
            request.getSession().removeAttribute("title");
            request.getSession().removeAttribute("color");
            request.getSession().removeAttribute("message");
            return LangManager.page("login/login",lang);

        }
        
        model.addAttribute("title", title);
        model.addAttribute("message", message);
        model.addAttribute("color", color);
        
        return LangManager.page("login/login",lang);
}
    
    @GetMapping("/register")
    public String register(Model model, HttpServletRequest request, @RequestParam(value = "lang", defaultValue = "it") String lang) {
    	User loggedUser = (User) request.getSession().getAttribute("loggedUser");
        model.addAttribute("loggedUser", loggedUser);
        request.getSession().setAttribute("lang", lang);

        captchaGenerator(request, model);
        
        model.addAttribute("userDto", new UserDto());
        return LangManager.page("login/register",lang);
    }
    @GetMapping("/registerReload")
    public String registerReload(Model model, HttpServletRequest request, @RequestParam(value = "lang", defaultValue = "it") String lang) {
    	User loggedUser = (User) request.getSession().getAttribute("loggedUser");
        model.addAttribute("loggedUser", loggedUser);
        request.getSession().setAttribute("lang", lang);

        captchaGenerator(request, model);
        
        model.addAttribute("userDto", new UserDto());
        return LangManager.page("login/register",lang);
    }
    
    @PostMapping("/register")
    public String registerUser(Model model, @ModelAttribute("userDto") UserDto userDto,
    		HttpServletRequest request, 
    		@RequestParam(value = "lang", defaultValue = "it") String lang) throws MessagingException, IOException, InterruptedException {
    	User loggedUser = (User) request.getSession().getAttribute("loggedUser");
        model.addAttribute("loggedUser", loggedUser);
        
        
    	String captchaSolution = (String) request.getSession().getAttribute("captchaSolution");
    	System.out.println(captchaSolution.toLowerCase());
    	if (!userDto.getCode().toLowerCase().equals(captchaSolution.toLowerCase())) {
    		LangManager.getErroreNelCaptchaInserito(lang,model);
    		
    		captchaGenerator(request, model);
            userDto.setCode(null);
            model.addAttribute("userForm",userDto);
            
            try {
    			WebhookManager.sendWebhook(WebhookLinkConfig.mikedmcConsole_errori, WebhookEmbedManager.embedRegisterCaptchaFailed(userDto.getUsername(), userDto.getEmail()));
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
            return LangManager.page("login/register",lang);
    	}
        
    	if (!userDto.getPassword().equals(userDto.getPassword2())) {
    		LangManager.getLePasswordNonCombaciano(lang, model);
    		
    		captchaGenerator(request, model);
            userDto.setCode(null);
            model.addAttribute("userForm",userDto);
            return LangManager.page("login/register",lang);
    	}
    	
        if (userService.userUsernameExists(userDto.getUsername())) {
    		LangManager.getNicknameGiaEsistente(lang, model);
            
    		captchaGenerator(request, model);
    		userDto.setCode(null);
            model.addAttribute("userForm",userDto);
            return LangManager.page("login/register",lang);
        }
        
        if (userService.userEmailExists(userDto.getUsername())) {
    		LangManager.getEmailGiaEsistente(lang, model);
        	
    		captchaGenerator(request, model);
            userDto.setCode(null);
            model.addAttribute("userForm",userDto);
            return LangManager.page("login/register",lang);
        }
        
        String otpCode = OTPCodeGenerator.generateOTPCode();
        try {
        	EmailManager.sendMail(userDto.getEmail(), userDto.getUsername(), otpCode, lang);        	
        } catch(com.sun.mail.smtp.SMTPAddressFailedException e) {
    		LangManager.getEmailNonValidaOInesistente(lang, model);

            captchaGenerator(request, model);
            userDto.setCode(null);
            model.addAttribute("userForm",userDto);
            return LangManager.page("login/register",lang);

        } catch(Exception e) {
        	e.printStackTrace();
        }

        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        user.setCreatedDate(LocalDateTime.now().plusHours(2));

        userService.saveUser(user);
        
        otpService.upsert(user, otpCode);
        
        // inviamo la mail all'utente con il codice.
		LangManager.getRegistrazioneCompletataOraConfermaEmail(lang, model);

		try {
			WebhookManager.sendWebhook(WebhookLinkConfig.mikedmcLogin_utenti, WebhookEmbedManager.embedRegister(userDto.getUsername(), userDto.getEmail()));
		} catch (Exception e) {
			e.printStackTrace();
		}

        return LangManager.page("login/registerOtp",lang);
    }
    
    @GetMapping("/otp")
    public String otpPage (Model model, @RequestParam("code") String code, HttpServletRequest request, @RequestParam(value = "lang", defaultValue = "it") String lang) {
    	User loggedUser = (User) request.getSession().getAttribute("loggedUser");
        model.addAttribute("loggedUser", loggedUser);
        request.getSession().setAttribute("lang", lang);
        
        if (code!=null) {
        	Otp otp = otpService.findByCode(code);
        	if (otp==null) {
        		LangManager.getCodiceOtpErratoONonValido(lang, model);
                
                return LangManager.page("login/registerOtp",lang);
        	}
        	User user = otp.getUser();
        	user.setConfirmed(true);
        	userService.update(user);
        	otpService.delete(otp);
        	
    		LangManager.getRegistrazioneCompletataOraEffettuaIlLogin(lang, model);
            
            return LangManager.page("login/login",lang);
    	}
        
        return LangManager.page("login/registerOtp",lang);
    }
    
    @PostMapping("/otp")
    public String otp(Model model, @RequestParam("code") String code, HttpServletRequest request, @RequestParam(value = "lang", defaultValue = "it") String lang) {
    	User loggedUser = (User) request.getSession().getAttribute("loggedUser");
        model.addAttribute("loggedUser", loggedUser);
        // cercare se l'otp è nel database, se c'è mettere su true l'utente, ed eliminare il codice in questione.
    	Otp otp = otpService.findByCode(code);
    	if (otp==null) {
    		LangManager.getCodiceOtpErratoONonValido(lang, model);
            
            return LangManager.page("login/registerOtp",lang);
    	}
    	User user = otp.getUser();
    	user.setConfirmed(true);
    	userService.update(user);
    	otpService.delete(otp);
    	        
		LangManager.getRegistrazioneCompletataOraPuoiEffettuareIlLogin(lang, model);
        
        return LangManager.page("login/login",lang);
    }
    
    @GetMapping("/reset-password")
    public String passwordReset (Model model, HttpServletRequest request, @RequestParam(value = "lang", defaultValue = "it") String lang) {
    	User loggedUser = (User) request.getSession().getAttribute("loggedUser");
        model.addAttribute("loggedUser", loggedUser);
        request.getSession().setAttribute("lang", lang);
        return LangManager.page("login/passwordResetOtpRequest",lang);
    }

    @PostMapping("/reset-password")
    public String passwordResetEmail (Model model, @RequestParam("email") String email, HttpServletRequest request, @RequestParam(value = "lang", defaultValue = "it") String lang)  throws MessagingException, IOException {
    	User loggedUser = (User) request.getSession().getAttribute("loggedUser");
        model.addAttribute("loggedUser", loggedUser);
        
        // fare anche il check che sia un email valida.
        if (email==null) {
    		LangManager.getDeviInserireUnEmail(lang, model);
            
        	model.addAttribute("formLogin","hidden");
            return LangManager.page("login/passwordResetOtpRequest",lang);
    	}
    	
    	// controllo se c'è l'email nel database.
    	User user = userService.findFromEmail(email);
    	if (user != null) {
    		// Invio codice otp all'email dell'utente.
            String otpCode = OTPCodeGenerator.generateOTPCode();

            otpService.upsert(user, otpCode);
            
            EmailManager.sendRecoveryMail(email, user.getUsername(), otpCode, lang);
            
            
    		LangManager.getVaiAControllareEmailPerIlCodiceOtp(lang, model);

            
            // reinderizzo allo step successivo con messaggio di successo.
            return LangManager.page("login/passwordResetOtp",lang);
    	}
    	
    	// errore non sei registrato.
		LangManager.getEmailNonRegistrata(lang, model);

		return LangManager.page("login/passwordResetOtpRequest",lang);
    }
    
    @GetMapping("/reset-password-confirm")
    public String passwordResetOtpAutomatico (Model model, HttpServletRequest request, @RequestParam("code") String code, @RequestParam(value = "lang", defaultValue = "it") String lang) {
    	User loggedUser = (User) request.getSession().getAttribute("loggedUser");
        model.addAttribute("loggedUser", loggedUser);
        request.getSession().setAttribute("lang", lang);

        if (code==null) {
    		LangManager.getErroreConIlCodiceOtp(lang, model);
            
            return LangManager.page("login/passwordResetOtp",lang);
        }
        
    	Otp otp = otpService.findByCode(code);
    	if (otp==null) {
    		return LangManager.page("login/passwordResetOtp",lang);
    	}
        request.getSession().setAttribute("resetPassword", otp.getUser());

		return LangManager.page("login/passwordResetOtp",lang);
    }
    
    @PostMapping("/reset-password-confirm")
    public String passwordResetOtp (Model model, HttpServletRequest request, @RequestParam("code") String code, @RequestParam(value = "lang", defaultValue = "it") String lang) {
    	User loggedUser = (User) request.getSession().getAttribute("loggedUser");
        model.addAttribute("loggedUser", loggedUser);
    	Otp otp = otpService.findByCode(code);
    	if (otp==null) {
    		return LangManager.page("login/passwordResetOtp",lang);
    	}
        request.getSession().setAttribute("resetPassword", otp.getUser());
            	
        return LangManager.page("login/passwordReset",lang);
    }
    
    @PostMapping("/resetting-password")
    public String passwordResetting (Model model, HttpServletRequest request,
    		@RequestParam("password") String password,
    		@RequestParam("password2") String password2, 
    		@RequestParam(value = "lang", defaultValue = "it") String lang) {
    	User loggedUser = (User) request.getSession().getAttribute("loggedUser");
        model.addAttribute("loggedUser", loggedUser);
        // controllo che siano uguali
    	if (!password.equals(password2)) {
    		LangManager.getLePasswordNonCombaciano(lang, model);
            return LangManager.page("login/passwordReset",lang);
    	}
    	
        User user = (User)request.getSession().getAttribute("resetPassword");
        // sovrascrivo la password dell'utente
    	user.setPassword(password);
    	userService.saveUser(user);
        
    	// gli mando l'allert
		LangManager.getLaPasswordEStataAggiornataOraPuoiEseguireIlLogin(lang, model);
        
    	return LangManager.page("login/login",lang);
    }
    
    private void captchaGenerator(HttpServletRequest request, Model model) {
    	HttpRequest request2 = HttpRequest.newBuilder()
                .uri(URI.create("https://captcha-generator.p.rapidapi.com/?noise_number=8&fontname=ubuntu"))
                .header("x-rapidapi-key", "ad156a94eamshda18db086165dafp13bb93jsndae5e5258fa5")
                .header("x-rapidapi-host", "captcha-generator.p.rapidapi.com")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = null;
        String solution = "";
        String imageUrl = "";
        try {
            response = HttpClient.newHttpClient().send(request2, HttpResponse.BodyHandlers.ofString());

            ObjectMapper objectMapper = new ObjectMapper();

            JsonNode rootNode = objectMapper.readTree(response.body());
            solution = rootNode.path("solution").asText();
            imageUrl = rootNode.path("image_url").asText();

            request.getSession().setAttribute("captchaSolution", solution);
            model.addAttribute("captchaImage",imageUrl);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
