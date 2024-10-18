package it.mikedmc.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import it.mikedmc.interceptor.PageViewInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private PageViewInterceptor pageViewInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Aggiungi l'Interceptor per il conteggio delle visualizzazioni delle pagine
        registry
		    .addInterceptor(pageViewInterceptor)
		    .addPathPatterns("/**")
		    .excludePathPatterns("/css/**", "/js/**", "/img/**", "/webjars/**", "/favicon.ico")
		    .excludePathPatterns("/cccsss");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Configurazione CORS
        registry.addMapping("/**")
            .allowedOrigins("*")
            .allowedMethods("GET", "POST")
            .allowedHeaders("*");
    }
}