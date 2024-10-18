package it.mikedmc.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;

import java.util.List;

import it.mikedmc.enums.AofRegion;

@Entity
public class AofMonster {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @Enumerated(EnumType.STRING)
    private AofRegion regione;

    private String linkPng;

    @OneToMany(mappedBy = "aofMonster", cascade = CascadeType.ALL)
    @OrderBy("rarita ASC")
    private List<AofDrop> drops;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public AofRegion getRegione() {
		return regione;
	}

	public void setRegione(AofRegion regione) {
		this.regione = regione;
	}

	public String getLinkPng() {
		return linkPng;
	}

	public void setLinkPng(String linkPng) {
		this.linkPng = linkPng;
	}

	public List<AofDrop> getDrops() {
		return drops;
	}

	public void setDrops(List<AofDrop> drops) {
		this.drops = drops;
	}
}

