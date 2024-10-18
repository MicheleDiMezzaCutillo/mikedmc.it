package it.mikedmc.config;

import it.mikedmc.MkConfig;

public class DatabaseConfig extends MkConfig<DatabaseConfig>{

	public DatabaseConfig(Class<DatabaseConfig> configClass) {
		super(configClass);
	}

	public static String mikedmcMusicanzaJdbcUrl;
    public static String mikedmcJdbcUser;
    public static String mikedmcJdbcPassword;
    public static String mikedmcJdbcDriver;
	
}
