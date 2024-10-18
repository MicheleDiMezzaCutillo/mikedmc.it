package it.mikedmc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import it.mikedmc.model.OtpDiscord;
import it.mikedmc.model.User;

public interface OtpDiscordRepository extends JpaRepository<OtpDiscord, Integer> {
	public OtpDiscord findByCode(String code);
	
	public OtpDiscord findByUser(User user);
}
