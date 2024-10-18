package it.mikedmc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.mikedmc.model.Otp;
import it.mikedmc.model.OtpDiscord;
import it.mikedmc.model.User;
import it.mikedmc.repository.OtpDiscordRepository;

@Service
public class OtpDiscordService {

	 @Autowired
	 private OtpDiscordRepository otpDisRep;

	 public void upsert(User user, String code) {
		 
		 OtpDiscord otp = otpDisRep.findByUser(user);
		 if (otp!=null) {
			 otpDisRep.delete(otp);
		 }
		 OtpDiscord newOtp = new OtpDiscord();
		 newOtp.setCode(code);
		 newOtp.setUser(user);
		 otpDisRep.save(newOtp);		 
	 }
	 
	 public OtpDiscord findByCode (String code) {
		 return otpDisRep.findByCode(code);
	 }
	 
	 public void delete (OtpDiscord otp) {
		 otpDisRep.delete(otp);
	 }
	
}