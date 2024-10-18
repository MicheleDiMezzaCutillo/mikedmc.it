package it.mikedmc.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.mikedmc.model.Processo;
import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/console")
public class Console2Controller {

	@PostMapping("/process/restart/{name}")
	public String edit(HttpServletRequest request, @PathVariable("name") String name) {
		// prendo il nome
		if (name==null || name.isBlank() || name.isEmpty()) {
			request.getSession().setAttribute("color", "red");
    	    request.getSession().setAttribute("title", "Errore");
        	request.getSession().setAttribute("message", "Il nome è corrotto.");
			
    	    return "redirect:/console?id=2";
		}
		
		Processo p = new Processo();
		String messaggioFinale = "";
		// cerco un processo con quel nome.
        try {
        	ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", "ps aux | grep " + name + " | grep -v grep");
        	processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;

                while ((line = reader.readLine()) != null) {
                    // Ignora le righe che contengono " S " (processi in sleep)
                    if (line.contains(" S ")) {
                        continue;
                    }

                    // Dividi la riga in base agli spazi
                    String[] parts = line.trim().split("\\s+");

                    // Assicurati che ci siano abbastanza parti nella riga
                    if (parts.length >= 8) {
                        String pid = parts[1]; // PID del processo
                        String memoryUsagePercentage = parts[3]; // Percentuale di utilizzo della memoria RAM
                        String physicalMemory = parts[4]; // RAM fisica utilizzata in kilobyte
                        String startTime = parts[8]; // Data di avvio del processo
                        String processName = parts[12];


                        String name2 = "";
                        if (processName.startsWith("java-")) {
                            name2 = processName.substring(5);
                        } else {
                            name2 = processName;
                        }

                        // Estrai il nome ritagliato
                        int index = name2.indexOf('-');
                        if (index != -1) {
                            name2 = name2.substring(0, index); // Prendi il nome fino al primo "-"
                        } else {
                            name2 = name2.substring(0,name2.length()-4); // Se non c'è "-", prendi il nome completo
                        }

                        p = new Processo(true, name2, processName, pid,memoryUsagePercentage,Integer.parseInt(physicalMemory),startTime);
                    }
                }
            }
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
		// attivo il processo con nohup java -jar name > log/nome_pulito.log &
        try {
        	
        	String nome_pulito="";
            if (name.toLowerCase().startsWith("java-")) {
            	nome_pulito = name.toLowerCase().substring(5);
            } else {
            	nome_pulito = name.toLowerCase();
            }

            // Estrai il nome ritagliato
            int index = nome_pulito.indexOf('-');
            if (index != -1) {
            	nome_pulito = nome_pulito.substring(0, index); // Prendi il nome fino al primo "-"
            } else {
            	nome_pulito = nome_pulito.substring(0,nome_pulito.length()-4); // Se non c'è "-", prendi il nome completo
            }
        	
            String command = String.format("java -jar %s > %s 2>&1 &", name, "log/"+nome_pulito+".log");

    		// accendo il name, e i log saranno il name tagliato e pulito.
            ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", command);
            Process process = processBuilder.start();

            // Attendi il completamento del processo
            int exitCode = process.waitFor();

    		// salvo su una variabile il risultato del comando (se è partito o no)
            // Controlla il codice di uscita
            if (exitCode == 0) {
                request.getSession().setAttribute("color", "green");
        	    request.getSession().setAttribute("title", "Successo");
            	messaggioFinale = "Acceso il processo "+name+".";
            } else {
                request.getSession().setAttribute("color", "red");
        	    request.getSession().setAttribute("title", "Errore");
                messaggioFinale = "Processo "+name+ " non è stato acceso. Codice: "+exitCode;
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        
		// se il pid era stato trovato,
		// kill -9 processo_trovato.
        if (p!=null) {
        	try {
                // Esegui il comando
                ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", "kill -9 "+p.getPid());
                Process process = processBuilder.start();

                // Attendi il completamento del processo
                int exitCode = process.waitFor();

                // Controlla il codice di uscita
                if (exitCode == 0) {
                	messaggioFinale += " Eliminato il processo precedente.";
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
		
		// aggiungiamo al messaggio in sessione
    	request.getSession().setAttribute("message", messaggioFinale);
	    return "redirect:/console?id=2";
	}

	@PostMapping("/process/switchoff/{pid}")
	public String turnoff(HttpServletRequest request,RedirectAttributes redirectAttributes, @PathVariable("pid") String pid) {
		
	    // Verifica se il PID è un numero valido
	    if (pid==null || pid.isBlank() || pid.isEmpty() || Integer.parseInt(pid)<=0) {
    	    request.getSession().setAttribute("color", "red");
    	    request.getSession().setAttribute("title", "Errore");
	    	request.getSession().setAttribute("message", "Il pid è corrotto.");
		    return "redirect:/console?id=2";
	    }

	    // Costruisci il comando per terminare il processo
	    ProcessBuilder processBuilder = new ProcessBuilder("kill", "-9", pid);

	    try {
	        // Esegui il comando
	        Process process = processBuilder.start();

	        // Aspetta che il comando venga completato
	        int exitCode = process.waitFor();
	        
	        // Controlla l'uscita del processo
	        if (exitCode == 0) {
	            // Processo terminato con successo
	        } else {
	    	    request.getSession().setAttribute("color", "red");
	    	    request.getSession().setAttribute("title", "Errore");
	        	request.getSession().setAttribute("message", "Il pid è corrotto.");
	    	    return "redirect:/console?id=2";
	        }
	    } catch (IOException | InterruptedException e) {
		    request.getSession().setAttribute("color", "red");
		    request.getSession().setAttribute("title", "Errore");
	        request.getSession().setAttribute("message", e.getStackTrace().toString());
		    return "redirect:/console?id=2";
	    }

	    request.getSession().setAttribute("message", "Processo "+pid+" terminato con successo.");
	    request.getSession().setAttribute("color", "green");
	    request.getSession().setAttribute("title", "Successo");
	    // Ricarica la pagina
	    return "redirect:/console?id=2";
	}
	
}
