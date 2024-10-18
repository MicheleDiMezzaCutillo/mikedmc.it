package it.mikedmc.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.mikedmc.model.AofMonster;
import it.mikedmc.model.AofNpc;
import it.mikedmc.repository.AofMonsterRepository;
import it.mikedmc.repository.AofNpcRepository;

import java.io.IOException;
import java.util.List;

@org.springframework.web.bind.annotation.RestController
public class RestController {
	
	@Autowired
    private AofNpcRepository aofNpcRepository;
	

	@Autowired
    private AofMonsterRepository aofMonsterRepository;
	
	 @GetMapping("/aof/download/npcs")
	    public ResponseEntity<String> downloadNpcs() {
	        List<AofNpc> npcs = aofNpcRepository.findAll();
	        ObjectMapper objectMapper = new ObjectMapper();
	        String json;

	        try {
	            json = objectMapper.writeValueAsString(npcs);
	        } catch (IOException e) {
	            return new ResponseEntity<>("Error generating JSON", HttpStatus.INTERNAL_SERVER_ERROR);
	        }

	        HttpHeaders headers = new HttpHeaders();
	        headers.add("Content-Disposition", "attachment; filename=npcs.json");
	        headers.add("Content-Type", "application/json");

	        return new ResponseEntity<>(json, headers, HttpStatus.OK);
	    }
	 
}
