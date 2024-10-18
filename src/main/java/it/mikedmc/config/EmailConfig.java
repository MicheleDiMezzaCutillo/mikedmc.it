package it.mikedmc.config;

import it.mikedmc.MkConfig;

public class EmailConfig extends MkConfig<EmailConfig>{

	public static String mikedmcMailUsername;
	public static String mikedmcMailPassword;

	public EmailConfig(Class<EmailConfig> configClass) {
		super(configClass);
	}

}
