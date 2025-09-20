package it.mikedmc;

import it.mikedmc.config.FileManagerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import it.mikedmc.config.DatabaseConfig;
import it.mikedmc.config.EmailConfig;
import it.mikedmc.webhook.WebhookLinkConfig;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		new EmailConfig(EmailConfig.class);
		new WebhookLinkConfig(WebhookLinkConfig.class);
		new DatabaseConfig(DatabaseConfig.class);
		new FileManagerConfig(FileManagerConfig.class);
		SpringApplication.run(Application.class, args);
	}

}
