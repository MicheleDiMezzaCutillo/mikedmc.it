package it.mikedmc.security;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import javax.security.sasl.AuthenticationException;

import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import it.mikedmc.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			org.springframework.security.core.AuthenticationException exception) throws IOException, ServletException {
		// Codifica i parametri per l'URL
        
        String lang = (String) request.getSession().getAttribute("lang"); 
        String title, message;
        String color = "red";
        switch(lang) {
    	case "en":
            title = URLEncoder.encode("Error!", StandardCharsets.UTF_8.toString());
            message = URLEncoder.encode("Invalid credentials.", StandardCharsets.UTF_8.toString());
            
            response.sendRedirect(String.format("/login?lang=en&title=%s&message=%s&color=%s",title, message, color));

    		break;
    	case "pl":
    		title = URLEncoder.encode("Błąd!", StandardCharsets.UTF_8.toString());
            message = URLEncoder.encode("Nieprawidłowe dane uwierzytelniające.", StandardCharsets.UTF_8.toString());
    		
            response.sendRedirect(String.format("/login?lang=pl&title=%s&message=%s&color=%s",title, message, color));
    		break;
    	case "ro":
    		title = URLEncoder.encode("Greșeli!", StandardCharsets.UTF_8.toString());
            message = URLEncoder.encode("Acreditări nevalide.", StandardCharsets.UTF_8.toString());
    		
            response.sendRedirect(String.format("/login?lang=ro&title=%s&message=%s&color=%s",title, message, color));

    		break;
		default:
    		title = URLEncoder.encode("Errore!", StandardCharsets.UTF_8.toString());
            message = URLEncoder.encode("Credenziali non valide.", StandardCharsets.UTF_8.toString());
    		
            response.sendRedirect(String.format("/login?title=%s&message=%s&color=%s",title, message, color));
			break;
    	}
	}
}