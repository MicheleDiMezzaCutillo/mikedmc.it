package it.mikedmc.lang;

import org.springframework.ui.Model;

public interface LangInterface {
	public void getLePasswordNonCombaciano(Model model);
	public void getNicknameGiaEsistente(Model model);
	public void getRegistrazioneCompletataOraConfermaEmail(Model model);
	public void getCodiceOtpErratoONonValido(Model model);
	public void getRegistrazioneCompletataOraEffettuaIlLogin(Model model);
	public void getRegistrazioneCompletataOraPuoiEffettuareIlLogin(Model model);
	public void getDeviInserireUnEmail(Model model);
	public void getVaiAControllareEmailPerIlCodiceOtp(Model model);
	public void getEmailNonRegistrata(Model model);
	public void getErroreConIlCodiceOtp(Model model);
	public void getLaPasswordEStataAggiornataOraPuoiEseguireIlLogin(Model model);
	public void getErroreNelCaptchaInserito(Model model);
	public void getEmailGiaEsistente(Model model);
	public void getEmailNonValidaOInesistente(Model model);
	public void getAttenzioneLinguaNonSupportata(Model model);
}
