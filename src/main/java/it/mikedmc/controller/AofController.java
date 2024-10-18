package it.mikedmc.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.mikedmc.enums.AofPlace;
import it.mikedmc.enums.AofRarity;
import it.mikedmc.enums.AofRegion;
import it.mikedmc.model.AofDrop;
import it.mikedmc.model.AofMonster;
import it.mikedmc.model.AofNpc;
import it.mikedmc.model.User;
import it.mikedmc.model.json.AofNpc2;
import it.mikedmc.model.json.MonsterData;
import it.mikedmc.repository.AofMonsterRepository;
import it.mikedmc.repository.AofNpcRepository;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class AofController {

	@Autowired
	private AofMonsterRepository repository;
	
	@Autowired
	private AofNpcRepository aofNpcRepository;
	
	@GetMapping("/aof/load")
    public String loadPage(Model model) {
        model.addAttribute("options", new String[]{"demoni", "npc"});
        return "aof/load"; // Nome del template Thymeleaf
    }

    @PostMapping("/aof/load")
    public String handleFileUpload(@RequestParam("type") String type, 
                                    @RequestParam("file") MultipartFile file) {
    	
    	ObjectMapper objectMapper = new ObjectMapper();
    	
    	System.out.println("Start");
    	
        // Logica per gestire il file e il tipo selezionato
        switch (type) {
            case "demoni":
            	try {
    		        // Leggi il file JSON dall'input MultipartFile
    		        List<MonsterData> monsterDataList = objectMapper.readValue(file.getInputStream(), new TypeReference<List<MonsterData>>() {});


    		                for (MonsterData data : monsterDataList) {
    		                    AofMonster monster = new AofMonster();
    		                    monster.setNome(data.getNome());
    		                    monster.setRegione(AofRegion.valueOf(data.getRegione().toUpperCase()));
    		                    monster.setLinkPng(data.getLinkPng());

    		                    List<AofDrop> drops = new ArrayList<>();
    		                    for (String dropString : data.getDrop()) {
    		                        String[] parts = dropString.split("~");
    		                        AofDrop drop = new AofDrop();
    		                        drop.setNome(parts[1]);
    		                        drop.setRarita(AofRarity.fromCodice(parts[0]).toString());
    		                        drop.setAofMonster(monster);
    		                        drops.add(drop);
    		                    }
    		                    monster.setDrops(drops);
    		                    repository.save(monster);
    		                }
    		    } catch (IOException e) {
    		        e.printStackTrace();
    		        return "error";
    		    }
            	
                break;
            case "npc":
                // Codice per gestire il caso "npc"
    	        try {
    	        	
    	            // Leggi il file JSON
    	            List<AofNpc> npcList = objectMapper.readValue(file.getInputStream(), new TypeReference<List<AofNpc>>() {});
    	            aofNpcRepository.saveAll(npcList);
    	            System.out.println("Salvati correttamente.");
    	        } catch (IOException e) {
    	            e.printStackTrace();
    	        }
                break;
            default:
                // Gestione di un caso non valido
                break;
        }
        
        System.out.println("Stop");
        return "redirect:/aof/load"; // Reindirizza alla pagina dopo il caricamento
    }
	
	@GetMapping("/aofLootTable")
	public String loottable (Model model, HttpServletRequest request) {
    	User loggedUser = (User) request.getSession().getAttribute("loggedUser");
        model.addAttribute("loggedUser", loggedUser);
    	
        model.addAttribute("monsters", repository.findAllByOrderByRegioneAsc());
        model.addAttribute("raritys", AofRarity.values());
        model.addAttribute("regions", AofRegion.values());
		return "aof/loottable/loottable";
	}
	
	 @GetMapping("/npcs")
	    public String npc(Model model, HttpServletRequest request) {
	    	User loggedUser = (User) request.getSession().getAttribute("loggedUser");
	        model.addAttribute("loggedUser", loggedUser);
	    	
	        model.addAttribute("npcs", aofNpcRepository.findAll());
	        model.addAttribute("places", AofPlace.values());
	        return "aof/npc/npcs";
	    }
	
}
