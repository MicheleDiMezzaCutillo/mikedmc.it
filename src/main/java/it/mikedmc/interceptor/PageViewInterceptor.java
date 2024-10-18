package it.mikedmc.interceptor;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import it.mikedmc.repository.PageViewRepository;
import it.mikedmc.webhook.WebhookEmbedManager;
import it.mikedmc.webhook.WebhookLinkConfig;
import it.mikedmc.webhook.WebhookManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class PageViewInterceptor implements HandlerInterceptor {

	@Autowired
	private PageViewRepository pvRepository;
	
    private final List<String> errorList = List.of("/error", "/login-required", "/403", "/404", "/500");
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String path = request.getRequestURI();
        
        // Scrivo nel db la pagina vista.
    	pvRepository.upsertPageView(path);

        // Controllo se il percorso è tra gli errori, e lo mando sul ds tramite webhook
        if (errorList.contains(path)) {
        	WebhookManager.sendWebhook(WebhookLinkConfig.mikedmcConsole_errori, WebhookEmbedManager.embedErrorePath(path));
        }

        // Continua con l'elaborazione della richiesta
        return true;
    }
}