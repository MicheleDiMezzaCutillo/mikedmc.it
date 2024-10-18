package it.mikedmc.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.mikedmc.model.PageView;
import it.mikedmc.model.Processo;
import it.mikedmc.model.User;
import it.mikedmc.repository.AdvertisingRepository;
import it.mikedmc.repository.ChangelogRepository;
import it.mikedmc.repository.CouponRedeemedRepository;
import it.mikedmc.repository.CouponRepository;
import it.mikedmc.repository.MusicanzaRepository;
import it.mikedmc.repository.PageViewRepository;
import it.mikedmc.repository.RoleRepository;
import it.mikedmc.service.UserService;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class ConsoleController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private ChangelogRepository changelogRepository;
	
	@Autowired
	private AdvertisingRepository advertisingRepository;
	
	@Autowired
	private PageViewRepository pageViewRepository;
	
	@Autowired
	private CouponRepository couponRepository;

	@Autowired
	private CouponRedeemedRepository couponRedeemedRepository;
	
    @GetMapping("/console")
    public String adminConsole(
    		Model model, 
    		HttpServletRequest request,
    		@RequestParam(name = "id", defaultValue = "1") String id) {
    	User loggedUser = (User) request.getSession().getAttribute("loggedUser");
        model.addAttribute("loggedUser", loggedUser);
        
        
        switch (id) {
        case "2":
        	String title = (String) request.getSession().getAttribute("title");
        	String message = (String) request.getSession().getAttribute("message");
        	String color = (String) request.getSession().getAttribute("color");
        	if (title!=null) {
        		model.addAttribute("title", title);
        		model.addAttribute("message", message);
        		model.addAttribute("color", color);        		
        	}
        	return due(model);
        case "3":
        	return tre(model);
        case "4":
        	return quattro(model);

        case "5":
        	return cinque(model);
        default:
        	return uno(model);	
        }
    }

    private String uno(Model model) {
    	int utenti = userService.getCount();
    	model.addAttribute("utenti", utenti); // rettangolino
    	model.addAttribute("utentiConfermati", userService.getCoutUserConfirmed());
    	model.addAttribute("utentiNonConfermati", userService.getCountUserNotConfirmed());
    	model.addAttribute("ruoli", roleRepository.count());
    	model.addAttribute("changelog", changelogRepository.count());
    	changelogRepository.findMostRecentChangelog();
    	LocalDateTime now = LocalDateTime.now().plusHours(2);
        LocalDateTime oneWeekAgo = now.minusWeeks(1);
        
        long utentiAttivi =userService.getActiveUserCount(oneWeekAgo, now);
    	model.addAttribute("utentiAttivi",utentiAttivi);
    	long utentiRecenti = userService.getUserCountCreatedBetween(oneWeekAgo, now);
    	model.addAttribute("utentiRecenti", utentiRecenti);

    	model.addAttribute("advertising",advertisingRepository.count());

    	model.addAttribute("perAttivi",String.format("border-bottom: 2px solid var(--accent); width: %.2f%%; text-align: left;", utenti > 0 ? ((double) utentiAttivi / utenti) * 100 : 1));
    	model.addAttribute("perRecenti",String.format("border-bottom: 2px solid var(--accent); width: %.2f%%; text-align: left;", utenti > 0 ? ((double) utentiRecenti / utenti) * 100 : 1));
    	model.addAttribute("ruoliNuovi", roleRepository.findTop3ByOrderByIdDesc());
		return "admin/console";
    }
    
	private String due(Model model) {
    	List<Processo> listaProcessi = new ArrayList<>();
    	List<String> listaJar = new ArrayList<>();
    	long percentuale = 0l;
    	
        File directory = new File("/root/");
        // Ottieni tutti i file nella directory
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                // Controlla se il file è un file .jar
                if (file.isFile() && file.getName().endsWith(".jar")) {
                    listaJar.add(file.getName());
                }
            }
        }

        try {
                ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", "ps aux | grep '[j]ava -jar'");
                processBuilder.redirectErrorStream(true);
                Process process = processBuilder.start();

                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    String line;

                    while ((line = reader.readLine()) != null) {
                        // Ignora le righe che contengono " S " (processi in sleep)
                        if (line.contains(" S ")) {
                            continue;
                        }
                        System.out.println(line);

                        // Dividi la riga in base agli spazi
                        String[] parts = line.trim().split("\\s+");

                        // Assicurati che ci siano abbastanza parti nella riga
                        if (parts.length >= 8) {
                            String pid = parts[1]; // PID del processo
                            String memoryUsagePercentage = parts[3]; // Percentuale di utilizzo della memoria RAM
                            //percentuale+=Long.parseLong(memoryUsagePercentage);
                            String physicalMemory = parts[4]; // RAM fisica utilizzata in kilobyte
                            String startTime = parts[8]; // Data di avvio del processo
                            String processName = parts[12];

                            if (listaJar.contains(processName)) {
                                listaJar.remove(processName);
                            }

                            String name = "";
                            if (processName.startsWith("java-")) {
                                name = processName.substring(5);
                            } else {
                                name = processName;
                            }

                            // Estrai il nome ritagliato
                            int index = name.indexOf('-');
                            if (index != -1) {
                                name = name.substring(0, index); // Prendi il nome fino al primo "-"
                            } else {
                                name = name.substring(0,name.length()-4); // Se non c'è "-", prendi il nome completo
                            }

                            listaProcessi.add(new Processo(true, name, processName, pid,memoryUsagePercentage,Integer.parseInt(physicalMemory),startTime));
                        }
                    }
                }
                process.waitFor();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }

        for (String jar : listaJar) {
            listaProcessi.add(new Processo(false,"",jar,"0000","0",0,"00:00"));
        }

        // Testing
        // listaProcessi.add(new Processo(true, "something","java-something-discordbot-1.0-SNAPSHOT-jar-with-dependencies.jar","2794","10",100000,"05:13"));
        // listaProcessi.add(new Processo(false,"","qualcosa.jar","0000","0",0,"00:00"));

        model.addAttribute("processList",listaProcessi);
        model.addAttribute("processiAttivi",listaProcessi.size()-listaJar.size());
        model.addAttribute("nProcessi",listaProcessi.size());
        model.addAttribute("processiNonAttivi",listaJar.size());
        model.addAttribute("percentualeRamUsata",percentuale);
		return "admin/console2";
    }
    
    private String tre(Model model) {
    	List<PageView> pageviews = pageViewRepository.findAll();
    	Collections.sort(pageviews, Comparator.comparing(PageView::getViewCount).reversed());
    	
    	// tutte le pageviews
    	model.addAttribute("pageviews",pageviews);
    	// quante pageviews abbiamo
    	model.addAttribute("count", pageViewRepository.count());
    	// visite totali (sommiamo tutto)
    	model.addAttribute("totalviews",pageViewRepository.getTotalViewCount());
    	
    	// top 3 visitate
    	model.addAttribute("top3",pageViewRepository.findTop3ByViewCountDesc());
    	// last 3 visitate?
    	model.addAttribute("bot3",pageViewRepository.findTop3ByViewCountAsc());

		return "admin/console3";
    }
    
    private String quattro(Model model) {
    	long total = couponRepository.countCoupons();
    	model.addAttribute("coupons", total);
    	long active = couponRepository.countActiveCoupons(LocalDateTime.now().plusHours(2));
    	model.addAttribute("couponActive", active);
    	model.addAttribute("couponInactive" , total-active);
    	
    	long totalCr = couponRedeemedRepository.countCoupons();
    	model.addAttribute("couponRedeemeds", totalCr);
    	long activeCr = couponRedeemedRepository.countActiveCoupons(LocalDateTime.now().plusHours(2));
    	model.addAttribute("couponRedeemedActive", activeCr);
    	model.addAttribute("couponRedeemedInactive" , totalCr-activeCr);
    	
    	
    	return "admin/console4";
    }
    

    private String cinque(Model model) {
    	MusicanzaRepository musicanzaRepository = MusicanzaRepository.getIstance();
    	
    	model.addAttribute("commands",musicanzaRepository.getTotaleContatore());
    	model.addAttribute("names",musicanzaRepository.getNumeroNomiDistinti());
    	model.addAttribute("serverNames",musicanzaRepository.getNumeroNomiServerDistinti());
    	
    	model.addAttribute("commandsAndCount", musicanzaRepository.getComandiESommaContatore());
    	model.addAttribute("namesAndCount", musicanzaRepository.getNomiESommaContatore());
    	model.addAttribute("serverNamesAndCount", musicanzaRepository.getNomeServerESommaContatore());
    	
    	return "admin/console5";
	}
    
    @PostMapping("/console")
    public String qualcosa () {
    	
		return "admin/console2";
    }

}
