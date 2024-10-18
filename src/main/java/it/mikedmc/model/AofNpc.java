package it.mikedmc.model;

import it.mikedmc.enums.AofPlace;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class AofNpc {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private int x;
    private int y;
    private int z;

    @Enumerated(EnumType.STRING)
    private AofPlace place;

    public AofNpc() {}
    
    public AofNpc(String nome, int x, int y, int z, AofPlace place) {
        this.nome = nome;
        this.x=x;
        this.y=y;
        this.z=z;
        this.place = place;
    }
    
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

	public AofPlace getPlace() {
		return place;
	}

	public void setPlace(AofPlace place) {
		this.place = place;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getZ() {
		return z;
	}

	public void setZ(int z) {
		this.z = z;
	}
	
}
