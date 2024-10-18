package it.mikedmc.lang;

import org.springframework.ui.Model;

public class RomanianLang implements LangInterface{

	@Override
	public void getLePasswordNonCombaciano(Model model) {
		model.addAttribute("title", "Eroare!");
		model.addAttribute("message", "Parolele nu se potrivesc.");

	}

	@Override
	public void getNicknameGiaEsistente(Model model) {
		model.addAttribute("title", "Eroare!");
		model.addAttribute("message", "Porecla există deja.");
	}
	
	@Override
	public void getEmailGiaEsistente(Model model) {
		model.addAttribute("title", "Eroare!");
		model.addAttribute("message", "Email deja existent.");
	}

	@Override
	public void getRegistrazioneCompletataOraConfermaEmail(Model model) {
		model.addAttribute("title", "Succes!");
		model.addAttribute("message", "Înregistrare completată, acum confirmă-ți emailul.");
	}

	@Override
	public void getCodiceOtpErratoONonValido(Model model) {
		model.addAttribute("title", "Eroare!");
		model.addAttribute("message", "Cod OTP incorect sau nevalid.");
	}

	@Override
	public void getRegistrazioneCompletataOraEffettuaIlLogin(Model model) {
		model.addAttribute("title", "Succes!");
		model.addAttribute("message", "Înregistrare completată, acum autentifică-te.");
	}

	@Override
	public void getRegistrazioneCompletataOraPuoiEffettuareIlLogin(Model model) {
		model.addAttribute("title", "Succes!");
		model.addAttribute("message", "Înregistrarea a fost finalizată, acum poți să te conectezi.");

	}

	@Override
	public void getDeviInserireUnEmail(Model model) {
		model.addAttribute("title", "Eroare!");
		model.addAttribute("message", "Trebuie să introduci o adresă de email.");
	}

	@Override
	public void getVaiAControllareEmailPerIlCodiceOtp(Model model) {
		model.addAttribute("title", "Succes!");
		model.addAttribute("message", "Verifică-ți emailul pentru codul OTP.");
	}

	@Override
	public void getEmailNonRegistrata(Model model) {
		model.addAttribute("title", "Eroare!");
		model.addAttribute("message", "Nu ești înregistrat.");
	}

	@Override
	public void getErroreConIlCodiceOtp(Model model) {
		model.addAttribute("title", "Eroare!");
		model.addAttribute("message", "Eroare cu codul OTP.");
	}

	@Override
	public void getLaPasswordEStataAggiornataOraPuoiEseguireIlLogin(Model model) {
		model.addAttribute("title", "Succes!");
		model.addAttribute("message", "Parola a fost actualizată; acum poți să te conectezi.");
	}

	@Override
	public void getErroreNelCaptchaInserito(Model model) {
		model.addAttribute("title", "Eroare!");
		model.addAttribute("message", "Captcha introdus nu se potrivește.");
	}

	@Override
	public void getEmailNonValidaOInesistente(Model model) {
		model.addAttribute("title", "Eroare!");
		model.addAttribute("message", "Emailul introdus nu este valid sau nu există.");
	}
	@Override
	public void getAttenzioneLinguaNonSupportata(Model model) {
		model.addAttribute("color", "yellow");
		model.addAttribute("title", "Alertă");
		model.addAttribute("message", "Limba nu este acceptată. Limba a fost resetată la italiană.");
	}
}
