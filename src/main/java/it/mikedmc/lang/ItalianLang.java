package it.mikedmc.lang;

import org.springframework.ui.Model;

public class ItalianLang implements LangInterface{

	@Override
	public void getLePasswordNonCombaciano(Model model) {
		model.addAttribute("title", "Errore!");
		model.addAttribute("message", "Le password non combaciano.");
	}

	@Override
	public void getNicknameGiaEsistente(Model model) {
		model.addAttribute("title", "Errore!");
        model.addAttribute("message", "Nickname già esistente.");
	}

	@Override
	public void getEmailGiaEsistente(Model model) {
		model.addAttribute("title", "Errore!");
        model.addAttribute("message", "Email già esistente.");
	}
	
	@Override
	public void getRegistrazioneCompletataOraConfermaEmail(Model model) {
		model.addAttribute("title", "Successo!");
        model.addAttribute("message", "Registrazione completata, ora conferma l'email.");
	}

	@Override
	public void getCodiceOtpErratoONonValido(Model model) {
		model.addAttribute("title", "Errore!");
		model.addAttribute("message", "Codice otp errato o non valido.");
	}

	@Override
	public void getRegistrazioneCompletataOraEffettuaIlLogin(Model model) {
		model.addAttribute("title", "Successo!");
        model.addAttribute("message", "Registrazione completata, ora effettua il login.");
	}

	@Override
	public void getRegistrazioneCompletataOraPuoiEffettuareIlLogin(Model model) {
		model.addAttribute("title", "Successo!");
        model.addAttribute("message", "Registrazione completata, ora puoi effettuare il login.");		
	}

	@Override
	public void getDeviInserireUnEmail(Model model) {
		model.addAttribute("title", "Errore!");
		model.addAttribute("message", "Devi inserire un'email.");
	}

	@Override
	public void getVaiAControllareEmailPerIlCodiceOtp(Model model) {
		model.addAttribute("title", "Successo!");
		model.addAttribute("message", "Vai a controllare l'email, per il codice OTP.");
	}

	@Override
	public void getEmailNonRegistrata(Model model) {
		model.addAttribute("title", "Errore!");
		model.addAttribute("message", "Email non registrata");
	}

	@Override
	public void getErroreConIlCodiceOtp(Model model) {
		model.addAttribute("title", "Errore!");
		model.addAttribute("message", "Errore con il codice OTP.");
	}

	@Override
	public void getLaPasswordEStataAggiornataOraPuoiEseguireIlLogin(Model model) {
		model.addAttribute("title", "Successo!");
		model.addAttribute("message", "La password è stata aggiornata, ora puoi eseguire il login.");
	}

	@Override
	public void getErroreNelCaptchaInserito(Model model) {
		model.addAttribute("title", "Errore!");
		model.addAttribute("message", "il Captcha inserito non combacia.");
	}

	@Override
	public void getEmailNonValidaOInesistente(Model model) {
		model.addAttribute("title", "Errore!");
		model.addAttribute("message", "L'email inserita non è valida o non esiste.");
	}
	
	@Override
	public void getAttenzioneLinguaNonSupportata(Model model) {}
}
