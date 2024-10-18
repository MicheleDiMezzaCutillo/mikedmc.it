package it.mikedmc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import it.mikedmc.model.Otp;
import it.mikedmc.model.User;

public interface OtpRepository extends JpaRepository<Otp, Integer> {
	public Otp findByCode(String code);
	
	public Otp findByUser(User user);
	
}
