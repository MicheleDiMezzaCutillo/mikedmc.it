package it.mikedmc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.mikedmc.model.Otp;
import it.mikedmc.model.User;
import it.mikedmc.repository.OtpRepository;

@Service
public class OtpService {

	 @Autowired
	 private OtpRepository otpRep;

	 public void upsert(User user, String code) {
		 
		 Otp otp = otpRep.findByUser(user);
		 if (otp!=null) {
			 otpRep.delete(otp);
		 }
		 Otp newOtp = new Otp();
		 newOtp.setCode(code);
		 newOtp.setUser(user);
		 otpRep.save(newOtp);		 
	 }
	 
	 public Otp findByCode (String code) {
		 return otpRep.findByCode(code);
	 }
	 
	 public void delete (Otp otp) {
		 otpRep.delete(otp);
	 }
	
}
