package it.mikedmc.webhook;

import org.json.JSONArray;
import org.json.JSONObject;

import it.mikedmc.model.AofDrop;
import it.mikedmc.model.AofMonster;
import it.mikedmc.model.User;

public class WebhookEmbedManager {
	public static String embedContattami(String username, String email, String emailRicontatto, String discordId, String thumbnailUrl, String messaggio) {
    	// Costruzione del payload JSON dinamicamente
        JSONObject embed = new JSONObject();
        embed.put("title", "Messaggio dal sito");
        
        String emails = (email.equals(emailRicontatto))?"**Email**: `"+email+"`\n":"**Email**: `"+email+"`\n**Email Ricontatto**: `"+emailRicontatto+"`\n";
        String discord = (discordId!=null) ? "**Discord**: <@" + discordId + ">\n":"";
        embed.put("description", "**Utente**: `" + username + "`\n" + emails+ discord+"**Messaggio**:\n```" + messaggio + "```");
        embed.put("color", 3447003);

        if (thumbnailUrl!=null) {
            JSONObject thumbnail = new JSONObject();
            thumbnail.put("url", thumbnailUrl);
            embed.put("thumbnail", thumbnail);
        }
        
        JSONArray embeds = new JSONArray();
        embeds.put(embed);
        JSONObject payload = new JSONObject();
        payload.put("embeds", embeds);
        return payload.toString();
    }

	public static String embedErrorePath(String path) {
	    // Costruzione del payload JSON dinamicamente
	    JSONObject embed = new JSONObject();
	    embed.put("title", "Messaggio dal sito");
	    
	    // Imposta la descrizione con il percorso della pagina
	    embed.put("description", "Ho mostrato questa pagina `" + path + "`");

	    // Imposta il colore rosso
	    embed.put("color", 16711680); // Red color in decimal

	    // Crea l'array degli embed e aggiungi il nostro embed
	    JSONArray embeds = new JSONArray();
	    embeds.put(embed);

	    // Crea l'oggetto payload e aggiungi l'array degli embed
	    JSONObject payload = new JSONObject();
	    payload.put("embeds", embeds);

	    // Restituisce il payload come stringa JSON
	    return payload.toString();
	}
	
	public static String embedLogin(String username, String email, String emailRicontatto, String discordId, String thumbnailUrl) {
    	// Costruzione del payload JSON dinamicamente
        JSONObject embed = new JSONObject();
        embed.put("title", "Accesso al sito");
        
        String emails = (email.equals(emailRicontatto))?"**Email**: `"+email+"`\n":"**Email**: `"+email+"`\n**Email Ricontatto**: `"+emailRicontatto+"`\n";
        String discord = (discordId!=null) ? "**Discord**: <@" + discordId + ">\n":"";
        embed.put("description", "**Utente**: `" + username + "`\n" + emails+ discord);
        embed.put("color", 3447003);

        if (thumbnailUrl!=null) {
            JSONObject thumbnail = new JSONObject();
            thumbnail.put("url", thumbnailUrl);
            embed.put("thumbnail", thumbnail);
        }
        
        JSONArray embeds = new JSONArray();
        embeds.put(embed);
        JSONObject payload = new JSONObject();
        payload.put("embeds", embeds);
        return payload.toString();
    }
	
	public static String embedRegister(String username, String email) {
    	// Costruzione del payload JSON dinamicamente
        JSONObject embed = new JSONObject();
        embed.put("title", "Nuova Registrazione al sito");
        embed.put("description", "**Utente**: `" + username + "`\n**Email**: `" + email+ "`");
        embed.put("color", 10693273);

        JSONArray embeds = new JSONArray();
        embeds.put(embed);
        JSONObject payload = new JSONObject();
        payload.put("embeds", embeds);
        return payload.toString();
    }
	

	public static String embedRegisterCaptchaFailed(String username, String email) {
		// Costruzione del payload JSON dinamicamente
        JSONObject embed = new JSONObject();
        embed.put("title", "Registrazione al sito fallita");
        embed.put("description", "**Utente**: `" + username + "`\n**Email**: `" + email+ "`");
        embed.put("color", 10693273);

        JSONArray embeds = new JSONArray();
        embeds.put(embed);
        JSONObject payload = new JSONObject();
        payload.put("embeds", embeds);
        return payload.toString();
	}


	public static String creatoDemone(User user, AofMonster monster) {
		JSONObject embed = new JSONObject();
        embed.put("title", "Creato nuovo Demone");
	    embed.put("description", "**Nome**: `" + monster.getNome() + "`\n**Regione**: `" + monster.getRegione() + "`");
        embed.put("color", 4039466);
        JSONObject footerObject = new JSONObject();
                footerObject.put("text", "Azione di | " + user.getUsername());
        embed.put("footer", footerObject);
        
        JSONObject thumbnailObject = new JSONObject();
        thumbnailObject.put("url", monster.getLinkPng());
        embed.put("thumbnail", thumbnailObject);
        
        
		JSONArray embeds = new JSONArray();
        embeds.put(embed);
        JSONObject payload = new JSONObject();
        payload.put("embeds", embeds);
		return payload.toString();
	}
	
	public static String modificatoDemone(User user, AofMonster monster) {
		JSONObject embed = new JSONObject();
        embed.put("title", "Modificato Demone");
	    embed.put("description", "**Nome**: `" + monster.getNome() + "`\n**Regione**: `" + monster.getRegione() + "`");
        embed.put("color", 10396458);
        JSONObject footerObject = new JSONObject();
                footerObject.put("text", "Azione di | " + user.getUsername());
        embed.put("footer", footerObject);
        
        JSONObject thumbnailObject = new JSONObject();
        thumbnailObject.put("url", monster.getLinkPng());
        embed.put("thumbnail", thumbnailObject);
        
        
		JSONArray embeds = new JSONArray();
        embeds.put(embed);
        JSONObject payload = new JSONObject();
        payload.put("embeds", embeds);
		return payload.toString();
	}
	
	public static String aggiuntoDropDemone(User user, AofDrop drop) {
		JSONObject embed = new JSONObject();
        embed.put("title", "Aggiunto nuovo Drop");
	    embed.put("description", "**Mostro**: `" + drop.getAofMonster().getNome() + "`\n**Drop**: `" + drop.getNome() + "`\n**Rarità**: `" + drop.getRarita() + "`");
        embed.put("color", 4039466);
        JSONObject footerObject = new JSONObject();
        footerObject.put("text", "Azione di | " + user.getUsername());
        embed.put("footer", footerObject);
        
        JSONObject thumbnailObject = new JSONObject();
        thumbnailObject.put("url", drop.getAofMonster().getLinkPng());
        embed.put("thumbnail", thumbnailObject);
        
        
		JSONArray embeds = new JSONArray();
        embeds.put(embed);
        JSONObject payload = new JSONObject();
        payload.put("embeds", embeds);
		return payload.toString();
	}
	
	public static String eliminatoDropDemone(User user, AofDrop drop) {
		JSONObject embed = new JSONObject();
        embed.put("title", "Eliminato Drop");
	    embed.put("description", "**Mostro**: `" + drop.getAofMonster().getNome() + "`\n**Drop**: `" + drop.getNome() + "`\n**Rarità**: `" + drop.getRarita() + "`");
        embed.put("color", 10693171);
        JSONObject footerObject = new JSONObject();
        footerObject.put("text", "Azione di | " + user.getUsername());
        embed.put("footer", footerObject);
        
        JSONObject thumbnailObject = new JSONObject();
        thumbnailObject.put("url", drop.getAofMonster().getLinkPng());
        embed.put("thumbnail", thumbnailObject);
        
        
		JSONArray embeds = new JSONArray();
        embeds.put(embed);
        JSONObject payload = new JSONObject();
        payload.put("embeds", embeds);
		return payload.toString();
	}
	
	public static String eliminatoDemone(User user, long i) {
		JSONObject embed = new JSONObject();
	    embed.put("title", "Elimianto Demone");
	    embed.put("description", "**Id**: `" + i + "`");
	    embed.put("color", 10693171);
	    JSONObject footerObject = new JSONObject();
	    footerObject.put("text", "Azione di | " + user.getUsername());
	    embed.put("footer", footerObject);
	    
		JSONArray embeds = new JSONArray();
	    embeds.put(embed);
	    JSONObject payload = new JSONObject();
	    payload.put("embeds", embeds);
		return payload.toString();
	}

	public static String modificatoDropDemone(User user, AofDrop drop) {
		JSONObject embed = new JSONObject();
        embed.put("title", "Modificato Drop");
	    embed.put("description", "**Mostro**: `" + drop.getAofMonster().getNome() + "`\n**Drop**: `" + drop.getNome() + "`\n**Rarità**: `" + drop.getRarita() + "`");
        embed.put("color", 10396458);
        JSONObject footerObject = new JSONObject();
        footerObject.put("text", "Azione di | " + user.getUsername());
        embed.put("footer", footerObject);
        
        JSONObject thumbnailObject = new JSONObject();
        thumbnailObject.put("url", drop.getAofMonster().getLinkPng());
        embed.put("thumbnail", thumbnailObject);
        
        
		JSONArray embeds = new JSONArray();
        embeds.put(embed);
        JSONObject payload = new JSONObject();
        payload.put("embeds", embeds);
		return payload.toString();
	}

}
