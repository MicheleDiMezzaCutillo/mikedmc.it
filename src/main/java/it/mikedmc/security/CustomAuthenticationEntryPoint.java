package it.mikedmc.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import it.mikedmc.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
    	// Salva il path della richiesta originale nella sessione
        String originalUrl = request.getRequestURI();
        request.getSession().setAttribute("loginAfterUrl", originalUrl);
        
        response.sendRedirect(request.getContextPath() + "/login-required");
    }
}
