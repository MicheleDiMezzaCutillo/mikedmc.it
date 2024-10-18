package it.mikedmc.lang;

import org.springframework.ui.Model;

public class PolishLang implements LangInterface{

	@Override
	public void getLePasswordNonCombaciano(Model model) {
		model.addAttribute("title", "Błąd!");
		model.addAttribute("message", "Hasła nie pasują do siebie.");
	}

	@Override
	public void getNicknameGiaEsistente(Model model) {
		model.addAttribute("title", "Błąd!");
		model.addAttribute("message", "Nazwa użytkownika już istnieje.");
	}

	@Override
	public void getEmailGiaEsistente(Model model) {
		model.addAttribute("title", "Błąd!");
		model.addAttribute("message", "Email już istnieje.");
	}

	@Override
	public void getRegistrazioneCompletataOraConfermaEmail(Model model) {
		model.addAttribute("title", "Sukces!");
		model.addAttribute("message", "Rejestracja zakończona, teraz potwierdź swój email.");
	}

	@Override
	public void getCodiceOtpErratoONonValido(Model model) {
		model.addAttribute("title", "Błąd!");
		model.addAttribute("message", "Nieprawidłowy lub nieważny kod OTP.");
	}

	@Override
	public void getRegistrazioneCompletataOraEffettuaIlLogin(Model model) {
		model.addAttribute("title", "Sukces!");
		model.addAttribute("message", "Rejestracja zakończona, teraz zaloguj się.");
	}

	@Override
	public void getRegistrazioneCompletataOraPuoiEffettuareIlLogin(Model model) {
		model.addAttribute("title", "Sukces!");
		model.addAttribute("message", "Rejestracja zakończona, teraz możesz się zalogować.");

	}

	@Override
	public void getDeviInserireUnEmail(Model model) {
		model.addAttribute("title", "Błąd!");
		model.addAttribute("message", "Musisz wpisać adres e-mail.");
	}

	@Override
	public void getVaiAControllareEmailPerIlCodiceOtp(Model model) {
		model.addAttribute("title", "Sukces!");
		model.addAttribute("message", "Sprawdź swoją skrzynkę e-mailową w celu otrzymania kodu OTP.");
	}

	@Override
	public void getEmailNonRegistrata(Model model) {
		model.addAttribute("title", "Błąd!");
		model.addAttribute("message", "Nie jesteś zarejestrowany.");
	}

	@Override
	public void getErroreConIlCodiceOtp(Model model) {
		model.addAttribute("title", "Błąd!");
		model.addAttribute("message", "Błąd z kodem OTP.");
	}

	@Override
	public void getLaPasswordEStataAggiornataOraPuoiEseguireIlLogin(Model model) {
		model.addAttribute("title", "Sukces!");
		model.addAttribute("message", "Hasło zostało zaktualizowane; teraz możesz się zalogować.");
	}

	@Override
	public void getErroreNelCaptchaInserito(Model model) {
		model.addAttribute("title", "Błąd!");
		model.addAttribute("message", "Wpisany Captcha nie pasuje.");
	}

	@Override
	public void getEmailNonValidaOInesistente(Model model) {
		model.addAttribute("title", "Błąd!");
		model.addAttribute("message", "Wpisany adres email jest nieprawidłowy lub nie istnieje.");
	}

	@Override
	public void getAttenzioneLinguaNonSupportata(Model model) {
		model.addAttribute("color", "yellow");
		model.addAttribute("title", "Uwaga");
		model.addAttribute("message", "Język nie jest obsługiwany. Język został zresetowany do włoskiego.");
	}
}
