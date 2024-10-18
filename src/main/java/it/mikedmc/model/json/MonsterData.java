package it.mikedmc.model.json;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MonsterData {
    
    @JsonProperty("Nome")
    private String nome;

    @JsonProperty("Regione")
    private String regione;

    @JsonProperty("Linkpng")
    private String linkPng;

    @JsonProperty("Drop")
    private List<String> drop;

    // Getters e Setters
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getRegione() {
        return regione;
    }

    public void setRegione(String regione) {
        this.regione = regione;
    }

    public String getLinkPng() {
        return linkPng;
    }

    public void setLinkPng(String linkPng) {
        this.linkPng = linkPng;
    }

    public List<String> getDrop() {
        return drop;
    }

    public void setDrop(List<String> drop) {
        this.drop = drop;
    }
}