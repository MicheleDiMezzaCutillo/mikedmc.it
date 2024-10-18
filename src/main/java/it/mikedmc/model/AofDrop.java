package it.mikedmc.model;

import it.mikedmc.enums.AofRarity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class AofDrop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Non usiamo @Enumerated per AofRarity, ma gestiamo manualmente il codice
    private String rarita;

    private String nome;

    @ManyToOne
    @JoinColumn(name = "aofMonster_id")
    private AofMonster aofMonster;

    // Metodi per gestire AofRarity tramite codice
    public AofRarity getRaritaEnum() {
        return AofRarity.fromCodice(this.rarita);
    }

    public void setRaritaEnum(AofRarity rarita) {
        this.rarita = rarita.getCodice();
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRarita() {
		return rarita;
	}

	public void setRarita(String rarita) {
		this.rarita = rarita;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public AofMonster getAofMonster() {
		return aofMonster;
	}

	public void setAofMonster(AofMonster aofMonster) {
		this.aofMonster = aofMonster;
	}
    
}
