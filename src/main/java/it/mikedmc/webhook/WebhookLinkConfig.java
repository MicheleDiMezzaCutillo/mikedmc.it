package it.mikedmc.webhook;

import it.mikedmc.MkConfig;

public class WebhookLinkConfig extends MkConfig<WebhookLinkConfig>{

	public WebhookLinkConfig(Class<WebhookLinkConfig> configClass) {
		super(configClass);
	}

	public static String mikedmcMessaggi_sito;
	
	public static String mikedmcConsole_errori;
	
	public static String mikedmcLogin_utenti;
	
	public static String mikedmcLoottable_modifiche;
}
