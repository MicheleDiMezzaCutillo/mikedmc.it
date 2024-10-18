package it.mikedmc.model.json;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AofNpc2 {
	@JsonProperty("nome")
    private String nome;

    @JsonProperty("coordinate")
    private String coordinate;

    @JsonProperty("pathImg")
    private String linkpathImg;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCoordinate() {
		return coordinate;
	}

	public void setCoordinate(String coordinate) {
		this.coordinate = coordinate;
	}

	public String getLinkpathImg() {
		return linkpathImg;
	}

	public void setLinkpathImg(String linkpathImg) {
		this.linkpathImg = linkpathImg;
	}
}
