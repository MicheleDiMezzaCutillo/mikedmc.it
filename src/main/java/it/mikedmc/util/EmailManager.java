package it.mikedmc.util;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.core.io.ClassPathResource;

import it.mikedmc.config.EmailConfig;

import javax.mail.MessagingException;

public class EmailManager {
	final static String mailUsername = EmailConfig.mikedmcMailUsername;
    final static String mailPassword = EmailConfig.mikedmcMailPassword;
	public static void sendMail (String destinatario, String username, String otpCode, String lang) throws MessagingException, IOException {
		
		Properties props = propertiesSettate();

        Session mailSession = Session.getInstance(props, null); // Creo la sessione

        Message msg = new MimeMessage( mailSession ); // Creo il messaggio.
        
        messaggioOtp(msg, destinatario, otpCode, username, lang);
        
        Transport transport = mailSession.getTransport("smtps"); // Imposto l'oggetto Transport con protocollo SMTPS (SMTP su SSL).
        
        transport.connect("smtp.libero.it", mailUsername, mailPassword); // Mi connetto al server SMTP con l'autenticatore.
        
        transport.sendMessage(msg, msg.getAllRecipients()); // Invia il mesaggio al server SMTP.
        transport.close(); // Chiudo la connessione al server SMTP.
		
	}
	
public static void sendMailDiscordLink (String destinatario, String username, String otpCode, String lang) throws MessagingException, IOException {
		
		Properties props = propertiesSettate();

        Session mailSession = Session.getInstance(props, null); // Creo la sessione

        Message msg = new MimeMessage( mailSession ); // Creo il messaggio.
        
        messaggioOtpDiscordLink(msg, destinatario, otpCode, username, lang);
        
        Transport transport = mailSession.getTransport("smtps"); // Imposto l'oggetto Transport con protocollo SMTPS (SMTP su SSL).
        
        transport.connect("smtp.libero.it", mailUsername, mailPassword); // Mi connetto al server SMTP con l'autenticatore.
        
        transport.sendMessage(msg, msg.getAllRecipients()); // Invia il mesaggio al server SMTP.
        transport.close(); // Chiudo la connessione al server SMTP.
		
	}
	
	public static void sendRecoveryMail (String destinatario, String username, String otpCode, String lang) throws MessagingException, IOException {
		Properties props = propertiesSettate();

        Session mailSession = Session.getInstance(props, null); // Creo la sessione

        Message msg = new MimeMessage( mailSession ); // Creo il messaggio.
        
        messaggioOtpRecovery(msg, destinatario, otpCode, username, lang);
        
        Transport transport = mailSession.getTransport("smtps"); // Imposto l'oggetto Transport con protocollo SMTPS (SMTP su SSL).
        
        transport.connect("smtp.libero.it", mailUsername, mailPassword); // Mi connetto al server SMTP con l'autenticatore.
        
        transport.sendMessage(msg, msg.getAllRecipients()); // Invia il mesaggio al server SMTP.
        transport.close(); // Chiudo la connessione al server SMTP.
		
	}
	
	
	public static Message messaggioOtp (Message msg, String ricevente, String otpCode, String username, String lang) throws AddressException, MessagingException, IOException {
	    final String TEMPLATE_DIR = "email-templates/";
		msg.setFrom( new InternetAddress(mailUsername) ); // Imposto il mittente del messaggio.
		String userDir = System.getProperty("user.dir");
        System.out.println("Current working directory: " + userDir);
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(ricevente));
        msg.setSubject("Your OTP Code");
        
        String templatePath = TEMPLATE_DIR + "otp_template_" + lang + ".html";
        
     // Carica il template utilizzando ClassPathResource
        ClassPathResource resource = new ClassPathResource(templatePath);
        String emailContent;
        try (InputStream inputStream = resource.getInputStream()) {
            emailContent = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                    .lines()
                    .reduce("", (accumulator, actual) -> accumulator + actual + "\n");
        }
        
        // Replace placeholders with actual data
        emailContent = emailContent.replace("{{name}}", username);
        emailContent = emailContent.replace("{{otpCode}}", otpCode);
        msg.setContent(emailContent, "text/html");

        msg.setSentDate(new Date()); // Imposto la data d'invio		
		
		return msg;
	}
	
	public static Message messaggioOtpDiscordLink (Message msg, String ricevente, String otpCode, String username, String lang) throws AddressException, MessagingException, IOException {
	    final String TEMPLATE_DIR = "email-templates/";
		msg.setFrom( new InternetAddress(mailUsername) ); // Imposto il mittente del messaggio.
		String userDir = System.getProperty("user.dir");
        System.out.println("Current working directory: " + userDir);
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(ricevente));
        msg.setSubject("Your OTP Code");
        
        String templatePath = TEMPLATE_DIR + "otp_discord_link_template_" + lang + ".html";
        
     // Carica il template utilizzando ClassPathResource
        ClassPathResource resource = new ClassPathResource(templatePath);
        String emailContent;
        try (InputStream inputStream = resource.getInputStream()) {
            emailContent = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                    .lines()
                    .reduce("", (accumulator, actual) -> accumulator + actual + "\n");
        }
        
        // Replace placeholders with actual data
        emailContent = emailContent.replace("{{name}}", username);
        emailContent = emailContent.replace("{{otpCode}}", otpCode);
        msg.setContent(emailContent, "text/html");

        msg.setSentDate(new Date()); // Imposto la data d'invio		
		
		return msg;
	}
	
	public static Message messaggioOtpRecovery (Message msg, String ricevente, String otpCode, String username, String lang) throws AddressException, MessagingException, IOException {
	    final String TEMPLATE_DIR = "email-templates/";
		msg.setFrom( new InternetAddress(mailUsername) ); // Imposto il mittente del messaggio.
		String userDir = System.getProperty("user.dir");
        System.out.println("Current working directory: " + userDir);
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(ricevente));
        msg.setSubject("Your OTP Code");
        
        String templatePath = TEMPLATE_DIR + "otp_recovery_template_" + lang + ".html";
        
     // Carica il template utilizzando ClassPathResource
        ClassPathResource resource = new ClassPathResource(templatePath);
        String emailContent;
        try (InputStream inputStream = resource.getInputStream()) {
            emailContent = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                    .lines()
                    .reduce("", (accumulator, actual) -> accumulator + actual + "\n");
        }
        
        // Replace placeholders with actual data
        emailContent = emailContent.replace("{{name}}", username);
        emailContent = emailContent.replace("{{otpCode}}", otpCode);
        msg.setContent(emailContent, "text/html");

        msg.setSentDate(new Date()); // Imposto la data d'invio		
		
		return msg;
	}
	
	public static Properties propertiesSettate() {
		Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.libero.it"); 
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.socketFactory.port", "587");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");
        return props;
	}
	
}
