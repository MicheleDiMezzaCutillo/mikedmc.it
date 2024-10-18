  package it.mikedmc.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;

@Entity
public class Changelog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private LocalDate date;

    @OneToMany(mappedBy = "changelog", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Log> logs = new ArrayList<>();
    
    @Transient
    private String formattedDate;
    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<Log> getLogs() {
        return logs;
    }

    public void setLogs(List<Log> logs) {
        this.logs = logs;
    }

    public void addLog(Log log) {
        logs.add(log);
        log.setChangelog(this);
    }

    public void removeLog(Log log) {
        logs.remove(log);
        log.setChangelog(null);
    }
    
	public String getFormattedDate() {
	    return formattedDate;
	}
	
	public Changelog setFormattedDate(String formattedDate) {
	    this.formattedDate = formattedDate;
	    return this;
	}
}
