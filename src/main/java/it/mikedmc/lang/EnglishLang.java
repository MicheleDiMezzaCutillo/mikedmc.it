package it.mikedmc.lang;

import org.springframework.ui.Model;

public class EnglishLang implements LangInterface{

	@Override
	public void getLePasswordNonCombaciano(Model model) {
		model.addAttribute("title", "Error!");
		model.addAttribute("message", "Passwords do not match.");
	}

	@Override
	public void getNicknameGiaEsistente(Model model) {
		model.addAttribute("title", "Error!");
		model.addAttribute("message", "Nickname already exists.");
	}

	@Override
	public void getEmailGiaEsistente(Model model) {
		model.addAttribute("title", "Error!");
		model.addAttribute("message", "Email already exists.");
	}

	@Override
	public void getRegistrazioneCompletataOraConfermaEmail(Model model) {
		model.addAttribute("title", "Success!");
		model.addAttribute("message", "Registration completed, now confirm your email.");
	}

	@Override
	public void getCodiceOtpErratoONonValido(Model model) {
		model.addAttribute("title", "Error!");
		model.addAttribute("message", "Incorrect or invalid OTP code.");

	}

	@Override
	public void getRegistrazioneCompletataOraEffettuaIlLogin(Model model) {
		model.addAttribute("title", "Success!");
		model.addAttribute("message", "Registration completed, now log in.");
	}

	@Override
	public void getRegistrazioneCompletataOraPuoiEffettuareIlLogin(Model model) {
		model.addAttribute("title", "Success!");
		model.addAttribute("message", "Registration completed, you can now log in.");

	}

	@Override
	public void getDeviInserireUnEmail(Model model) {
		model.addAttribute("title", "Error!");
		model.addAttribute("message", "You must enter an email address.");
	}

	@Override
	public void getVaiAControllareEmailPerIlCodiceOtp(Model model) {
		model.addAttribute("title", "Success!");
		model.addAttribute("message", "Please check your email for the OTP code.");
	}

	@Override
	public void getEmailNonRegistrata(Model model) {
		model.addAttribute("title", "Error!");
		model.addAttribute("message", "You are not registered.");
	}

	@Override
	public void getErroreConIlCodiceOtp(Model model) {
		model.addAttribute("title", "Error!");
		model.addAttribute("message", "Error with the OTP code.");
	}

	@Override
	public void getLaPasswordEStataAggiornataOraPuoiEseguireIlLogin(Model model) {
		model.addAttribute("title", "Success!");
		model.addAttribute("message", "The password has been updated; you can now log in.");
	}

	@Override
	public void getErroreNelCaptchaInserito(Model model) {
		model.addAttribute("title", "Error!");
		model.addAttribute("message", "The entered Captcha does not match.");
	}

	@Override
	public void getEmailNonValidaOInesistente(Model model) {
		model.addAttribute("title", "Error!");
		model.addAttribute("message", "The entered email is invalid or does not exist.");
	}

	@Override
	public void getAttenzioneLinguaNonSupportata(Model model) {
		model.addAttribute("color", "yellow");
		model.addAttribute("title", "Warning");
		model.addAttribute("message", "Unsupported language. The language has been reset to Italian.");
	}

}
