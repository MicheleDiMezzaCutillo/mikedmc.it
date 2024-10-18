package it.mikedmc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.mikedmc.model.Changelog;
import it.mikedmc.model.Log;
import it.mikedmc.repository.ChangelogRepository;
import it.mikedmc.repository.LogRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ChangelogService {

    @Autowired
    private ChangelogRepository changelogRepository;

    @Autowired
    private LogRepository logRepository;

    public List<Changelog> getAllChangelogs() {
        return changelogRepository.findAll();
    }

    public Changelog getChangelogById(Integer id) {
        return changelogRepository.findById(id).orElse(null);
    }

    public Changelog getChangelogByDate(LocalDate date) {
        return changelogRepository.findByDate(date).orElse(null);
    }

    public Changelog getLatestChangelog() {
    	Changelog changelog = changelogRepository.findTopByOrderByDateDesc().orElse(null);
    	if (changelog==null) {
    		return null;
    	}
        String formattedDate = changelog.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        changelog.setFormattedDate(formattedDate);
        return changelog;
    }

    public Integer createChangelog(LocalDate date, List<String> logMessages) {
        Changelog changelog = new Changelog();
        changelog.setDate(date);

        for (String message : logMessages) {
            Log log = new Log();
            log.setMessage(message);
            changelog.addLog(log);
        }

        Changelog savedChangelog = changelogRepository.save(changelog);
        return savedChangelog.getId();
    }

    public void updateChangelog(Integer id, LocalDate date, List<String> logMessages) {
        Changelog changelog = getChangelogById(id);
        if (changelog != null) {
            changelog.setDate(date);

            // Rimuovi i log esistenti
            changelog.getLogs().clear();

            // Aggiungi i nuovi log
            for (String message : logMessages) {
                Log log = new Log();
                log.setMessage(message);
                changelog.addLog(log);
            }

            changelogRepository.save(changelog);
        }
    }

    public void deleteChangelog(Integer id) {
        if (changelogRepository.existsById(id)) {
            changelogRepository.deleteById(id);
        }
    }
    
    public Changelog getMostRecentChangelog() {
        return changelogRepository.findMostRecentChangelog()
                .orElse(null);
    }
}