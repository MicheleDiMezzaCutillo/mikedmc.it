package it.mikedmc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class DiscordController {

	@GetMapping("/discord")
	public RedirectView reindirizza (@RequestParam(value = "lang", defaultValue = "it") String lang) {

		switch(lang) {
		case "en":
			return new RedirectView("https://discord.gg/9SF8yxtUr7");
		default:
			return new RedirectView("https://discord.gg/amyVP9uSHa");
		}
	}
	
}
