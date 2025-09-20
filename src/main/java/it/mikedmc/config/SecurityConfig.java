package it.mikedmc.config;

import it.mikedmc.model.User;
import it.mikedmc.model.UserRole;
import it.mikedmc.model.CouponRedeemed;
import it.mikedmc.model.CouponRole;
import it.mikedmc.model.Role;
import it.mikedmc.repository.UserRepository;
import it.mikedmc.security.CustomAuthenticationSuccessHandler;
import it.mikedmc.service.CouponRedeemedService;
import it.mikedmc.security.CustomAuthenticationEntryPoint;
import it.mikedmc.security.CustomAuthenticationFailureHandler;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomAuthenticationSuccessHandler successHandler;
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("https://mikedmc.it");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.ignoringRequestMatchers(
                        new AntPathRequestMatcher("/api/public/**"),
                        new AntPathRequestMatcher("/api/admin/**")
                ))
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/", "/login", "/css/*", "/js/*", "/img/*","/img/progetti/*", "/img/niente/*","/home", "/register", "/logout","/login-required", "/message/*","/otp","/reset-password", "/reset-password-confirm", "/resetting-password","/discordbot", "/discordserver", "/telegrambot", "/niente", "/progetti", "/discord", "/changelogs", "/termsAndConditions", "/mikedmc.itF73FC5DB1A1E6A87F3EB02FB3709583A.txt", "/cookies", "/coupon/use", "/docs", "/docs/*", "/csv/*","/test", "/api/public/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/admin/**").hasAuthority("ROLE_Console")
                        .requestMatchers(HttpMethod.POST,"/api/admin/**").hasAuthority("ROLE_Console")
                        .requestMatchers(HttpMethod.PUT,"/api/admin/**").hasAuthority("ROLE_Console")
                        .requestMatchers(HttpMethod.PATCH,"/api/admin/**").hasAuthority("ROLE_Console")
                        .requestMatchers(HttpMethod.DELETE,"/api/admin/**").hasAuthority("ROLE_Console")
                        .requestMatchers("/console", "/console/process/switchoff/*", "/console/process/restart/*","/roles","/roles/*","/users","/users/*","/changelog","/changelog/*", "/advertising", "/advertising/*", "/aof/load", "/aof/download/npcs", "/aofmonster","/aofmonster/*", "/coupon","/coupon/*", "/couponRedemption", "/couponRedemption/*", "/console/docs").hasAuthority("ROLE_Console")

                        .requestMatchers("/aofmonster","/aofmonster/*","/aofdrop","/aofdrop/*").hasAuthority("ROLE_LootTableStaff")
                        .requestMatchers("/aofloottables").hasAuthority("ROLE_LootTable")
                        .requestMatchers("/aofnpc", "/aofnpc/*").hasAuthority("ROLE_NpcStaff")
                        .requestMatchers("/npcs").hasAuthority("ROLE_Npc")
                        .anyRequest().authenticated())
                .formLogin(login -> {
                	login
                		 .loginPage("/login") // Path to your login page
                         .loginProcessingUrl("/login") // URL to handle login requests
                         .successHandler(successHandler)
                         .failureHandler(new CustomAuthenticationFailureHandler()) // Aggiungi questo
                         .permitAll();})
                .exceptionHandling(
                    		handling -> handling
                    		.accessDeniedHandler(accessDeniedHandler())
                    		.authenticationEntryPoint(authenticationEntryPoint())
                    		);
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()));

        return http.build();
    }

    @Autowired
    private CouponRedeemedService couponRedeemedService;
    
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            User user = userRepository.findByUsernameOrEmailAndConfirmed(username);
            if (user == null) {
                throw new UsernameNotFoundException("User not found");
            }
            Set<String> ruoli = user.getUserRoles()
                    .stream()
                    .map(ur -> "ROLE_" + ur.getRole().getDescription())
                    .collect(Collectors.toSet());
            
            List<CouponRedeemed> ruoliExtra = couponRedeemedService.findCouponRoles(user);
            if (ruoliExtra != null) {
            	for (CouponRedeemed c : ruoliExtra) {
            		for (CouponRole cr : c.getCoupon().getCouponRoles()) {
            			ruoli.add("ROLE_" + cr.getRole().getDescription());
            		}
            	}
            }

            return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(ruoli.stream().toArray(String[]::new))
                .build();
        };
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        AccessDeniedHandlerImpl accessDeniedHandler = new AccessDeniedHandlerImpl();
        accessDeniedHandler.setErrorPage("/403");
        return accessDeniedHandler;
    }
    
    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new CustomAuthenticationEntryPoint();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

