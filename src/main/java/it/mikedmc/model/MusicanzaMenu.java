package it.mikedmc.model;

public class MusicanzaMenu {
	private int id;
    private String title;
    private String link;

    public MusicanzaMenu(int id, String title, String link) {
        this.id = id;
        this.title = title;
        this.link = link;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }
}
