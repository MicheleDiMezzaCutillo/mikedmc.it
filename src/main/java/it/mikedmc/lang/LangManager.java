package it.mikedmc.lang;

import org.springframework.ui.Model;

public class LangManager {
	
	public static String page(String page, String lang) {
		if ("it".equals(lang)) {
            return page;
        } else {
            return page + "-" + lang;
        }
	}
	
	public static LangInterface getLang(String lang) {
        switch (lang) {
            case "en":
                return new EnglishLang();
            case "pl":
                return new PolishLang();
            case "ro":
                return new RomanianLang();
            default:
            	return new ItalianLang();
        }
    }
	
	public static void getLePasswordNonCombaciano(String lang, Model model) {
		model.addAttribute("color", "red");
		getLang(lang).getLePasswordNonCombaciano(model);
	}

	public static void getNicknameGiaEsistente(String lang, Model model) {
        model.addAttribute("color", "red");
		getLang(lang).getNicknameGiaEsistente(model);
	}

	public static void getRegistrazioneCompletataOraConfermaEmail(String lang, Model model) {
        model.addAttribute("color", "green");
		getLang(lang).getRegistrazioneCompletataOraConfermaEmail(model);
	}

	public static void getCodiceOtpErratoONonValido(String lang, Model model) {
        model.addAttribute("color", "red");
		getLang(lang).getCodiceOtpErratoONonValido(model);
	}

	public static void getRegistrazioneCompletataOraEffettuaIlLogin(String lang, Model model) {
        model.addAttribute("color", "green");
		getLang(lang).getRegistrazioneCompletataOraEffettuaIlLogin(model);
	}

	public static void getRegistrazioneCompletataOraPuoiEffettuareIlLogin(String lang, Model model) {
        model.addAttribute("color", "green");
		getLang(lang).getRegistrazioneCompletataOraPuoiEffettuareIlLogin(model);
	}

	public static void getDeviInserireUnEmail(String lang, Model model) {
        model.addAttribute("color", "red");
		getLang(lang).getDeviInserireUnEmail(model);
	}

	public static void getVaiAControllareEmailPerIlCodiceOtp(String lang, Model model) {
        model.addAttribute("color", "green");
		getLang(lang).getVaiAControllareEmailPerIlCodiceOtp(model);
	}

	public static void getEmailNonRegistrata(String lang, Model model) {
        model.addAttribute("color", "red");
		getLang(lang).getEmailNonRegistrata(model);
	}

	public static void getErroreConIlCodiceOtp(String lang, Model model) {
        model.addAttribute("color", "red");
		getLang(lang).getErroreConIlCodiceOtp(model);
	}

	public static void getLaPasswordEStataAggiornataOraPuoiEseguireIlLogin(String lang, Model model) {
        model.addAttribute("color", "green");
		getLang(lang).getLaPasswordEStataAggiornataOraPuoiEseguireIlLogin(model);
	}

	public static void getErroreNelCaptchaInserito(String lang, Model model) {
		model.addAttribute("color", "red");
		getLang(lang).getErroreNelCaptchaInserito(model);
	}

	public static void getEmailGiaEsistente(String lang, Model model) {
		model.addAttribute("color", "red");
		getLang(lang).getEmailGiaEsistente(model);
	}

	public static void getEmailNonValidaOInesistente(String lang, Model model) {
		model.addAttribute("color", "red");
		getLang(lang).getEmailNonValidaOInesistente(model);
	}

	public static void getAttenzioneLinguaNonSupportata(String lang, Model model) {
		getLang(lang).getAttenzioneLinguaNonSupportata(model);
	}
	
}
